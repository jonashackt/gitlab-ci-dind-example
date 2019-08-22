package de.jonashackt.restexamples.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restexamples")
public class Controller {

    public static final String BRANCH_RESPONSE = "The git branch name is: ";
    @Value("${git.branch}")
    private String branch;

    public static final String RESPONSE = "Hello Rest-User!";

    @RequestMapping(path="/hello", method=RequestMethod.GET)
    public String helloWorld() {
        System.out.println("Rocking REST!");
    	return RESPONSE;
    }

    @RequestMapping(path="/branchname", method=RequestMethod.GET)
    public String branchName() {
        System.out.println("Obtaining git branch name from git-commit-id-plugin generated git.properties: " + branch);
    	return BRANCH_RESPONSE + branch;
    }
}
