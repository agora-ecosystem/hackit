/*
 * Copyright 2022 DIMA/TU-Berlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tuberlin.dima.hackit.server.controller;

import de.tuberlin.dima.hackit.server.model.Job;
import de.tuberlin.dima.hackit.server.model.Operator;
import de.tuberlin.dima.hackit.server.model.Plan;
import de.tuberlin.dima.hackit.server.model.Platform;
import de.tuberlin.dima.hackit.server.repository.JobRepository;
import de.tuberlin.dima.hackit.server.repository.OperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/wayang")
public class WayangController {

    JobRepository jobRepository;

    OperatorRepository operatorRepository;
    @Autowired
    public WayangController() {
        this.jobRepository = JobRepository.getInstance();
        this.operatorRepository = OperatorRepository.getInstance();
    }

    //==================================================================================================================
    //==================================================================================================================
    //======                                                                                                       =====
    //======                                       UTILS                                                           =====
    //======                                                                                                       =====
    //==================================================================================================================
    //==================================================================================================================

    @GetMapping("version")
    public String getVersion() {
        return "0.1";
    }

    //==================================================================================================================
    //==================================================================================================================
    //======                                                                                                       =====
    //======                                       OPERATORS                                                       =====
    //======                                                                                                       =====
    //==================================================================================================================
    //==================================================================================================================

    @GetMapping(value = "operator", produces = "application/json")
    public Collection<Operator> getList() {
        return this.operatorRepository.getOperators();
    }

    @PostMapping(value = "operator", consumes = "application/json", produces = "application/json")
    public Operator addOperator(Operator operator) {
        return this.operatorRepository.addOperator(operator);
    }

    @DeleteMapping(value = "operator", consumes = "application/json", produces = "application/json")
    public Operator deleteOperator(Operator operator) {
        return this.operatorRepository.deleteOperator(operator);
    }

    @GetMapping(value = "operator/{operatorId}", produces = "application/json")
    public Operator getOperator(@PathVariable("operatorId") String operatorId) {
        return this.operatorRepository.getOperator(operatorId);
    }


    //==================================================================================================================
    //==================================================================================================================
    //======                                                                                                       =====
    //======                                       PLATFORM                                                        =====
    //======                                                                                                       =====
    //==================================================================================================================
    //==================================================================================================================

    @GetMapping(value = "platform", produces = "application/json")
    public Platform[] getPlatforms() {
        return Platform.values();
    }

    //==================================================================================================================
    //==================================================================================================================
    //======                                                                                                       =====
    //======                                            JOBS                                                       =====
    //======                                                                                                       =====
    //==================================================================================================================
    //==================================================================================================================

    @GetMapping(value = "job", produces = "application/json")
    public Collection<Job> getJobs() {
        return this.jobRepository.getAllJobs();
    }

    @PostMapping(value = "job", consumes = "application/json", produces = "application/json")
    public Job addJob(Job job) {
        return this.jobRepository.addJob(job);
    }

    @DeleteMapping(value = "job", consumes = "application/json", produces = "application/json")
    public Job deleteJob(Job job) {
        return this.jobRepository.removeJob(job.getId());
    }

    @GetMapping(value = "job/{jobId}", produces = "application/json")
    public Job getJob(@PathVariable("jobId") String jobId) {
        return this.jobRepository.getJob(jobId);
    }


    //==================================================================================================================
    //==================================================================================================================
    //======                                                                                                       =====
    //======                                            PLANS                                                      =====
    //======                                                                                                       =====
    //==================================================================================================================
    //==================================================================================================================

    @GetMapping(value = "job/{jobId}/plan", produces = "application/json")
    public Collection<Plan> getPlans(@PathVariable("jobId") String jobId) {
        return List.of(this.jobRepository.getJob(jobId).getPlans());
    }

    @PostMapping(value = "job/{jobId}/plan", consumes = "application/json", produces = "application/json")
    public Job addPlan(@PathVariable("jobId") String jobId, Plan plan) {
        return this.jobRepository.getJob(jobId).addPlan(plan);
    }

    @DeleteMapping(value = "job/{jobId}/plan", consumes = "application/json", produces = "application/json")
    public Job deletePlan(@PathVariable("jobId") String jobId, Plan plan) {
        return this.jobRepository.getJob(jobId).removePlan(plan.getId());
    }

    @GetMapping(value = "job/{jobId}/plan/{planId}", produces = "application/json")
    public Plan getPlan(@PathVariable("jobId") String jobId, @PathVariable("planId") String planId) {
        return this.jobRepository.getJob(jobId).getPlan(planId);
    }


}
