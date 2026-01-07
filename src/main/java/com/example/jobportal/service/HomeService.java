package com.example.jobportal.service;

import com.example.jobportal.entity.JobApplication;
import com.example.jobportal.entity.JobOpenings;
import com.example.jobportal.repository.JobApplicationRepository;
import com.example.jobportal.repository.JobOpeningsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobOpeningsRepository jobOpeningsRepository;

    public HomeService(JobApplicationRepository jobApplicationRepository, JobOpeningsRepository jobOpeningsRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobOpeningsRepository = jobOpeningsRepository;
    }

    public JobOpenings getJobById(Long jobId) {
        return jobOpeningsRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }


    public JobApplication saveApplication(JobApplication application) {
        application.setStatus("APPLIED");
        return jobApplicationRepository.save(application);
    }

    public List<JobApplication> getAllApplications() {
        return jobApplicationRepository.findAll();
    }

    public void updateStatus(Long applicationId, String status) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(status);
        jobApplicationRepository.save(application);
    }
    public List<JobApplication> getApplicationsByStatus(String status) {
        return jobApplicationRepository.findByStatus(status);
    }

}
