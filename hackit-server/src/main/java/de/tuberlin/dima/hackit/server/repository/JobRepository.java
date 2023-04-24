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

package de.tuberlin.dima.hackit.server.repository;

import de.tuberlin.dima.hackit.server.model.Job;

import java.util.Collection;
import java.util.HashMap;


public class JobRepository {
    private static JobRepository INSTANCE;
    private HashMap<String, Job> jobs;

    private JobRepository() {
        jobs = new HashMap<>();
    }

    public static JobRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JobRepository();
        }
        return INSTANCE;
    }

    public Job addJob(Job job) {
        jobs.put(job.getId(), job);
        return job;
    }

    public Collection<Job> getAllJobs() {
        return jobs.values();
    }

    public Job getJob(String id) {
        return jobs.get(id);
    }

    public Job removeJob(String id) {
        return jobs.remove(id);
    }

    public void clear() {
        jobs.clear();
    }

    public Job updateJob(String id, Job job) {
        jobs.put(id, job);
        return job;
    }
}

