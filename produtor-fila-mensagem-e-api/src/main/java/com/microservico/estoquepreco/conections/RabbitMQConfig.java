package com.microservico.estoquepreco.conections;

import constantes.RabbitmqConstantes;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

// Configuration faz mais sentido para essa classe do que component
@Configuration
public class RabbitMQConfig {
  private static final String NOME_EXCHANGE = "amq.direct";

  private final AmqpAdmin amqpAdmin;

  public RabbitMQConfig(AmqpAdmin amqpAdmin){
    this.amqpAdmin = amqpAdmin;
  }

/**
 * Implementação antiga
 */
//  private Queue fila(String nomeFila){
//    return new Queue(nomeFila, true, false, false);
//  }
//
//  private DirectExchange trocaDireta() {
//    return new DirectExchange(NOME_EXCHANGE);
//  }
//
//  private Binding relacionamento(Queue fila, DirectExchange troca){
//    return new Binding(fila.getName(), Binding.DestinationType.QUEUE, troca.getName(), fila.getName(), null);
//  }

  @Bean
  Queue filaEstoque() {
    return new Queue(RabbitmqConstantes.FILA_ESTOQUE, false);
  }

  @Bean
  Queue filaPreco() {
    return new Queue(RabbitmqConstantes.FILA_PRECO, false);
  }

  @Bean
  DirectExchange exchange() {
    return new DirectExchange(NOME_EXCHANGE);
  }

  @Bean
  Binding bindingEstoque(@Qualifier("filaEstoque") Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(queue.getName());
  }

  //exemplo mais limpo
  @Bean
  Binding bindingPreco(DirectExchange exchange) {
    return BindingBuilder.bind(filaPreco()).to(exchange).with(filaPreco().getName());
  }

  //está função é executada assim que nossa classe é instanciada pelo Spring
  @PostConstruct
  private void adiciona(){
//    Queue filaEstoque = this.fila(RabbitmqConstantes.FILA_ESTOQUE);
//    Queue filaPreco   = this.fila(RabbitmqConstantes.FILA_PRECO);
//
//    DirectExchange troca = this.trocaDireta();
//
//    Binding ligacaoEstoque = this.relacionamento(filaEstoque, troca);
//    Binding ligacaoPreco   = this.relacionamento(filaPreco, troca);
//
    //Criando as filas no RabbitMQ
    this.amqpAdmin.declareQueue(filaEstoque());
    this.amqpAdmin.declareQueue(filaPreco());
//    this.amqpAdmin.declareQueue(filaEstoque);
//    this.amqpAdmin.declareQueue(filaPreco);
//
//    this.amqpAdmin.declareExchange(troca);
//
//    this.amqpAdmin.declareBinding(ligacaoEstoque);
//    this.amqpAdmin.declareBinding(ligacaoPreco);
  }
}
