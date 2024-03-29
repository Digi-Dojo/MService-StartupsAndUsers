package com.startupsdigidojo.usersandteams.startup.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class that serves to handle the get requests on startups
 */
@Service
public class SearchStartups {

    private final StartupRepository startupRepository;

    @Autowired
    public SearchStartups(StartupRepository startupRepository) {
        this.startupRepository = startupRepository;
    }

    /**
     * @param id id of the startup we want to find
     * @return the startup with the provided id
     * @throws IllegalArgumentException if no startup with the provided id is found
     */
    public Startup findById(Long id){
        Optional<Startup> maybeStartup = startupRepository.findById(id);

        if(maybeStartup.isEmpty()){
            throw new IllegalArgumentException("Startup with id " + id + " is not present in the database");
        }

        return maybeStartup.get();
    }

    /**
     * @return all the startups in the database
     * @throws IllegalArgumentException if no startup is present in the database
     */
    public List<Startup> findAll(){
        List<Startup> list = startupRepository.findAll();
        if(list.isEmpty()){
            throw new IllegalArgumentException("No startups in database");
        }
        return list;
    }

    /**
     * @param name name of the startup we want to find
     * @return the startup with the provided name
     * @throws IllegalArgumentException if no startup with the provided name is found
     */
    public Startup findByName(String name){
        Optional<Startup> maybeStartup = startupRepository.findByName(name);

        if(maybeStartup.isEmpty()){
            throw new IllegalArgumentException("Startup with name " + name + " does not exist");
        }

        return maybeStartup.get();
    }

}
