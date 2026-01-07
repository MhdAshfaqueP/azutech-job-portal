package com.example.jobportal.service;


import com.example.jobportal.entity.JobOpenings;
import com.example.jobportal.repository.JobOpeningsRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class JobOpeningsService {

    private final JobOpeningsRepository jobOpeningsRepository;

    @Autowired
    public JobOpeningsService(JobOpeningsRepository jobOpeningsRepository){
        this.jobOpeningsRepository = jobOpeningsRepository;
    }

    public JobOpenings addJobs(JobOpenings jobOpenings) {

        return jobOpeningsRepository.save(jobOpenings);
    }

    public List<JobOpenings> getAllJobs(){

        return jobOpeningsRepository.findAll();
    }

    public boolean deleteJobById(Long jobId) {
        if (jobOpeningsRepository.existsById(jobId)) {
            jobOpeningsRepository.deleteById(jobId);
            return true;
        }
        return false;
    }

}
