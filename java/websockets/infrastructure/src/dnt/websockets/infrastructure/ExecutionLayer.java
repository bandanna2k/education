package dnt.websockets.infrastructure;

/*

- Execution layer
  - Responsible for matching requests/responses using correlation IDs
  - Real life, client has its own, server has its own
  - Integration combines the 2 execution layers together.
  - Created on connection, along with text message handlers
  - Requests use vertx futures
    - Can choose to complete immediately, or defer until later
  - Opportunity to intercept messages.

- Client/Server Text Message Handlers
  - Handle the serde
  - Pass off message to request/processors
  - Same ones used for real and integration

- Request Processors
  - Follow the visitor design pattern to get from AbstractMessage to desired functionality. (see RequestProcessor)
  - Same ones used for real and integration
  - Created in the text message handlers
  - Debugging

 */
public interface ExecutionLayer extends ServerExecutionLayer, ClientExecutionLayer
{
}
