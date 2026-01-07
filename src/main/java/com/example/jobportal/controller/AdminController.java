package com.example.jobportal.controller;

import com.example.jobportal.entity.Admin;
import com.example.jobportal.entity.JobApplication;
import com.example.jobportal.entity.JobOpenings;
import com.example.jobportal.service.AdminService;
import com.example.jobportal.service.HomeService;
import com.example.jobportal.service.JobOpeningsService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    private final AdminService adminService;
    private final HomeService homeService;
    private final JobOpeningsService jobOpeningsService;

    @Autowired
    public AdminController(AdminService adminService, HomeService homeService, JobOpeningsService jobOpeningsService) {
        this.adminService = adminService;
        this.homeService = homeService;
        this.jobOpeningsService = jobOpeningsService;
    }

    @GetMapping("/admin/login")
    public String showLoginPage() {
        return "loginpage";
    }

    @PostMapping("/admin/login")
    public String loginAdmin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        Admin admin = adminService.login(username, password);

        if (admin != null) {
            // simple session
            session.setAttribute("admin", admin);
            return "redirect:/admin-dashboard";
        }

        model.addAttribute("error", "Invalid username or password");
        return "loginpage";
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard(HttpSession session) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        return "admin-dashboard";
    }

    @PostMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // remove session
        return "redirect:/admin/login";
    }

    @GetMapping("/admin/jobs")
        public String showManageJobsPage(Model model, HttpSession session) {

            // OPTIONAL: session check (if not using Spring Security)
            if (session.getAttribute("admin") == null) {
                return "redirect:/admin/login";
            }

            model.addAttribute("jobs", jobOpeningsService.getAllJobs());
            model.addAttribute("jobopenings", new JobOpenings());
            return "manage-jobs";
        }

    @PostMapping("/addJobs")
    public String saveUser(@ModelAttribute JobOpenings jobOpenings, RedirectAttributes redirectAttributes, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        jobOpeningsService.addJobs(jobOpenings);
        redirectAttributes.addFlashAttribute(
                "message", "Job added successfully!"
        );
        return "redirect:/admin/jobs";
    }

    @PostMapping("/deleteJobs")
    public String deleteJobs(@RequestParam Long jobId,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        if (jobOpeningsService.deleteJobById(jobId)) {
            redirectAttributes.addFlashAttribute("message", "Job deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Job not found!");
        }

        return "redirect:/admin/jobs";
    }

    @GetMapping("/admin/applications")
    public String showApplications(
            @RequestParam(required = false) String status,
            Model model,
            HttpSession session
    ) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        if (status != null) {
            model.addAttribute("applications",
                    homeService.getApplicationsByStatus(status));
            model.addAttribute("pageTitle", status + " Applications");
        } else {
            model.addAttribute("applications",
                    homeService.getAllApplications());
            model.addAttribute("pageTitle", "All Applications");
        }

        return "view-applications";
    }

    @PostMapping("/admin/applications/accept")
    public String acceptApplication(
            @RequestParam Long applicationId,
            HttpSession session
    ) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        homeService.updateStatus(applicationId, "ACCEPTED");
        return "redirect:/admin/applications";
    }

    @PostMapping("/admin/applications/reject")
    public String rejectApplication(
            @RequestParam Long applicationId,
            HttpSession session
    ) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        homeService.updateStatus(applicationId, "REJECTED");
        return "redirect:/admin/applications";
    }

}
