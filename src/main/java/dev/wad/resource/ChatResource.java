package dev.wad.resource;

import dev.wad.service.Chatbot;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/chat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatResource {

    @Inject
    Chatbot chatbot;

    @POST
    public Response chat(ChatRequest request) {
        try {
            String response = chatbot.chat(request.message());
            return Response.ok(new ChatResponse(response)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ChatResponse("Error: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/health")
    public Response health() {
        return Response.ok(new ChatResponse("Chat service is running")).build();
    }

    public record ChatRequest(String message) {}
    public record ChatResponse(String response) {}
}
