package com.instituto.main.web.controller;


import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		
//		RestTemplate restTemplate = new RestTemplate();
		
		//Pegando pelo ID
		
//		Cedula cedula
//			= restTemplate.getForObject("http://localhost:8088/caixaWS/cedula/1", Cedula.class);
//		
//		System.out.println(cedula.getValor());
		
		
		//DEPOSITANDO NO BANCO
//		List<Cedula> cedulas = new ArrayList<Cedula>();
//		Cedula cedula = new Cedula();
//		cedula.setValor(2);
//		cedula.setQuantidade(100);
//		cedulas.add(cedula);
//		
//		Boolean response = restTemplate.postForObject(
//				  "http://localhost:8088/caixaWS/cedula/depositar",
//				  cedulas,
//				  Boolean.class);
//
//		
//		System.out.println(response);
		
		//SACANDO NO BANCO
//		ResponseEntity<List<Cedula>> response = restTemplate.exchange(
//		  "http://localhost:8088/caixaWS/cedula/sacar/92",
//		  HttpMethod.GET,
//		  null,
//		  new ParameterizedTypeReference<List<Cedula>>(){});
//
//		List<Cedula> cedulas = response.getBody();
//		
//		for(int i = 0; i < cedulas.size(); i++) {
//			System.out.println(cedulas.get(i).getValor()+" "+cedulas.get(i).getQuantidade());
//		}
		
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
