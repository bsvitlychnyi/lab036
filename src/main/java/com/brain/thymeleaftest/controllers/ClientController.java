package com.brain.thymeleaftest.controllers;

import com.brain.thymeleaftest.dto.FilterDto;
import com.brain.thymeleaftest.entities.Client;
import com.brain.thymeleaftest.repositories.ClientRepository;
import javax.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.beans.Transient;

@Controller
@AllArgsConstructor
public class ClientController {

    private final ClientRepository clientRepository;


    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "phone", required = false, defaultValue = "") String filter,
                        @RequestParam(name = "sort", required = false, defaultValue = "") String sort) {
        if (sort.equals("") || sort.equals("DESC")){
            sort = "ASC";
            if (!filter.equals("")){
                model.addAttribute("clients", clientRepository.findByPhoneAsc(filter));
            }else{
                model.addAttribute("clients", clientRepository.findAllDesc());
            }
        }else{
            sort = "DESC";
            if (!filter.equals("")){
                model.addAttribute("clients", clientRepository.findByPhoneDesc(filter));
            }else{
                model.addAttribute("clients", clientRepository.findAllAsc());
            }
        }
        model.addAttribute("filter", new FilterDto(filter));
        model.addAttribute("sort", sort);
        return "index";
    }

    @GetMapping("/new")
    public String showSignUpForm( Model model) {
        model.addAttribute("client", new Client());
        return "add-client";
    }

    @PostMapping("/add")
    public String addClient(@Valid Client client,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-client";
        }
        clientRepository.save(client);
        Iterable<Client> clients = clientRepository.findAll();
        model.addAttribute("clients", clients);
        model.addAttribute("filter", new FilterDto());
        model.addAttribute("sort", "ASC");
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid client Id:" + id));
        model.addAttribute("client", client);
        return "update-client";
    }

    @PostMapping("/update/{id}")
    public String updateClient(@PathVariable("id") long id,
                               @Valid Client client, BindingResult result, Model model) {
        if (result.hasErrors()) {
            client.setId(id);
            return "update-client";
        }
        clientRepository.save(client);
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("filter", new FilterDto());
        model.addAttribute("sort", "ASC");
        return "index";
    }

    @Transient
    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable("id") long id, Model model) {
        clientRepository.deleteById(id);
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("filter", new FilterDto());
        model.addAttribute("sort", "ASC");
        return "index";
    }
}
