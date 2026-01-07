package com.example.jobportal.controller;

import com.example.jobportal.entity.JobApplication;
import com.example.jobportal.entity.JobOpenings;
import com.example.jobportal.service.HomeService;
import com.example.jobportal.service.JobOpeningsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class HomeController {

    private final HomeService homeService;
    private final JobOpeningsService jobOpeningsService;

    public HomeController(HomeService homeService, JobOpeningsService jobOpeningsService ) {
        this.homeService = homeService;
        this.jobOpeningsService = jobOpeningsService;
    }

    // Home page
    @GetMapping("/")
    public String home() {
        return "home";
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/availableJobs")
    public String showJobs(Model model) {
        model.addAttribute("jobs", jobOpeningsService.getAllJobs());
        return "availableJobs";
    }

    @PostMapping("/availableJobs")
    public String applyJob(@RequestParam("jobId") Long jobId,
                           Model model) {

        JobOpenings job = homeService.getJobById(jobId);

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);

        model.addAttribute("jobApplication", jobApplication);
        model.addAttribute("job", job);

        return "applicationForm";
    }

    @GetMapping("/apply/{jobId}")
    public String showApplicationForm(@PathVariable Long jobId, Model model) {

        JobOpenings job = homeService.getJobById(jobId);

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job); // IMPORTANT

        model.addAttribute("jobApplication", jobApplication);
        model.addAttribute("job", job);

        return "applicationForm";
    }


    // Submit application
    @PostMapping("/submitApplication")
    public String submitApplication(
            @ModelAttribute JobApplication jobApplication,
            @RequestParam("resumeFile") MultipartFile resumeFile
    ) throws IOException {

        if (!resumeFile.isEmpty()) {

            String basePath = "D:/SpringWeb/portal/uploads/resumes/";
            File directory = new File(basePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalFilename = resumeFile.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String uniqueFileName = UUID.randomUUID() + fileExtension;

            String fullPath = basePath + uniqueFileName;
            resumeFile.transferTo(new File(fullPath));

            jobApplication.setResumePath(fullPath);
        }

        homeService.saveApplication(jobApplication);
        return "redirect:/apply-success";
    }

    @GetMapping("/apply-success")
    public String applicationSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Your application has been submitted successfully!"
        );
        return "redirect:/availableJobs";
    }

}
