package com.pxo.snips;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestForm;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("rest/todo")
public class quarksvk4 {
    @Inject
    Logger logger;

    public AtomicLong sequence = new AtomicLong(0);
    public record ToDo (long id, String text, boolean completed){}

    public record Error(String target, String message){}
    public record Errors(int code,List<Error> list){}
    public record Result(long id, boolean success, List<Error> errors){}

    public static Result ofSuccess(long id){
        return new Result(id,true, List.of());
    }

    private final List<ToDo> all = new ArrayList<>();

    @PostConstruct
    void init(){
       all.add(new ToDo(sequence.incrementAndGet(),"First", false));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public List<ToDo> list() {
        return this.all;
    }

    public static class AddTodo{
        @RestForm String todo;
        @RestForm String author;

        @Override
        public String toString() {
            return "AddTodo{" +
                    "text='" + todo + '\'' +
                    '}';
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Result addTodo(@BeanParam AddTodo addTodo) {
        String todo = addTodo.todo;
        String author = addTodo.author;

        final String name = ""+todo;
        //data.getValues().get("todo").stream().map(fv->fv.getValue()).collect(Collectors.joining());

        logger.infof("adding %s %s",todo, author);
        ToDo newOne = new ToDo(sequence.incrementAndGet(), name,false);
        this.all.add(newOne);
        return ofSuccess(newOne.id());
    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/{id}")
    public Result editTodo(@PathParam("id")long id, @BeanParam AddTodo addTodo) {
        String todo = addTodo.todo;
        String author = addTodo.author;

        final String name = ""+todo;


        logger.infof("editing %s %s",todo, author);
        ToDo newOne = new ToDo(id, name,false);
        for(int i=0;i<this.all.size();i++){
            ToDo t= this.all.get(i);
            if(t.id()==newOne.id()){
                this.all.set(i,newOne);
                return ofSuccess(newOne.id());
            }
        }
        return ofError("id not found");
    }

    private Result ofError(String error) {
        return new Result(-1,false,List.of(new Error("id",error)));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Result removeTodo(@PathParam("id") long id) {
        var newList = this.all.stream().filter(t->t.id!=id).toList();
        all.clear();
        all.addAll(newList);
        return ofSuccess(id);
    }
    /*@DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/all")
    public Result removeTodo() {
        this.all.clear();
        return ofSuccess(-1);
    }*/
}
