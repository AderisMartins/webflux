package br.com.martins.webflux.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String message){
        super(message);
    }
}
