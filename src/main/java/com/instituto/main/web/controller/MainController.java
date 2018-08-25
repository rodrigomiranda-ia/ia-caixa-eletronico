package com.instituto.main.web.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.instituto.main.model.Cedula;

@Controller
@RequestMapping("/caixa")
public class MainController {

	@GetMapping(value="/home")
	public ModelAndView index() {
		
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("cedula", new Cedula());
		
		return modelAndView;
	}
	
	@PostMapping(value="/sacar")
	@ResponseBody
	public String sacar(@RequestParam Integer valor) {
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<List<Cedula>> response = restTemplate.exchange(
		  "http://localhost:8088/caixaWS/cedula/sacar/"+valor,
		  HttpMethod.GET,
		  null,
		  new ParameterizedTypeReference<List<Cedula>>(){});
		
		String resultado = "";
		if(response.hasBody()) {
			List<Cedula> cedulas = response.getBody();
			for(int i = 0; i < cedulas.size(); i++) {
				resultado = resultado + "Valor: R$ "+cedulas.get(i).getValor()
						+",00"+" | Quantidade de Notas: "+cedulas.get(i).getQuantidade()+"\n";
			}
		} else {
			resultado = "Não foi possível realizar o saque.";
		}
		
		return resultado;
	}
	
	@PostMapping(value="/depositar")
	@ResponseBody
	public String depositar(@RequestParam Integer quantidade2,
			@RequestParam Integer quantidade5, @RequestParam Integer quantidade10,
			@RequestParam Integer quantidade20, @RequestParam Integer quantidade50) {
		
		RestTemplate restTemplate = new RestTemplate();
		
		List<Cedula> cedulas = new ArrayList<Cedula>();
		
		Cedula cedulaDe2 = new Cedula();
		cedulaDe2.setValor(2);
		cedulaDe2.setQuantidade(quantidade2);
		cedulas.add(cedulaDe2);
		
		Cedula cedulaDe5 = new Cedula();
		cedulaDe5.setValor(5);
		cedulaDe5.setQuantidade(quantidade5);
		cedulas.add(cedulaDe5);
		
		Cedula cedulaDe10 = new Cedula();
		cedulaDe10.setValor(10);
		cedulaDe10.setQuantidade(quantidade10);
		cedulas.add(cedulaDe10);
		
		Cedula cedulaDe20 = new Cedula();
		cedulaDe20.setValor(20);
		cedulaDe20.setQuantidade(quantidade20);
		cedulas.add(cedulaDe20);
		
		Cedula cedulaDe50 = new Cedula();
		cedulaDe50.setValor(50);
		cedulaDe50.setQuantidade(quantidade50);
		cedulas.add(cedulaDe50);
		
		Boolean response = restTemplate.postForObject(
		  "http://localhost:8088/caixaWS/cedula/depositar",
		  cedulas,
		  Boolean.class);
		
		String resultado = "";
		if(response) {
			resultado = "Depósito realizado com sucesso.";
		} else {
			resultado = "Não foi possível realizar o depósito.";
		}
		
		return resultado;
	}
	
	@GetMapping(value="/pegarTodasNotas")
	@ResponseBody
	public String pegarTodasAsNotas() {
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<List<Cedula>> response = restTemplate.exchange(
		  "http://localhost:8088/caixaWS/cedula/QuantidadeNotaTO",
		  HttpMethod.GET,
		  null,
		  new ParameterizedTypeReference<List<Cedula>>(){});

		List<Cedula> cedulas = response.getBody();
		String resultado = "";
		for(int i = 0; i < cedulas.size(); i++) {
			resultado = resultado + "Valor: R$ "+cedulas.get(i).getValor()
					+",00"+" | Quantidade de Notas: "+cedulas.get(i).getQuantidade()+"\n";
		}
		
		return resultado;
	}
	
	@GetMapping(value="/consultarSaldo")
	@ResponseBody
	public String consultarSaldo() {
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> response = restTemplate.exchange(
		  "http://localhost:8088/caixaWS/cedula/saldo",
		  HttpMethod.GET,
		  null,
		  String.class);
		
		String resultado = "";
		if(response.hasBody()) {
			resultado = "R$ "+response.getBody().substring(9, response.getBody().length()-1)+",00";
		} else {
			resultado = "Ocorreu um erro";
		}
		return resultado;
	}
}
