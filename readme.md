# Protocollo di Comunicazione Chat-Java

## Lista di chiamate possibili

### Login

#### Client
- Invio username e password
  ```json
  { "type": "loginRequest", "username": "...", "password": "..." }
  ```

#### Server
- **Errore**
  - Indica l'errore avvenuto durante login/registrazione
    ```json
    { "type": "loginError", "message": "..." }
    ```
- **Successo**
  - Invia lo user appena loggato
    ```json
    { "type": "loggedUser", "id": "...", "username": "...", "profilePicture": "..." }
    ```
  - Invia le chat associate allo user appena loggato
    ```json
    { "type": "chat", "id": "...", "name": "...", "photo": "...", "users": [...] }
    ```
    > Nota: `users` può essere null o un array di oggetti `[{"id":"...","username":"..."}, ...]`


### Registrazione

#### Client
- Invio username, password e immagine profilo
  ```json
  { "type": "registrationRequest", "username": "...", "password": "...", "profilePicture": "..." }
  ```

#### Server
- **Errore**
  - Indica l'errore avvenuto durante login/registrazione
    ```json
    { "type": "registerError", "message": "..." }
    ```
- **Successo**
  - Invia lo user appena registrato
    ```json
    { "type": "registerSucces", "message": "..." }
    ```

### Homepage

#### INTERAZIONE CON CHAT (utente clicca su una chat)
- **Client**
  - Invia id_Chat al server
    ```json
    { "type": "messagesRequest", "chatId": "..." }
    ```
- **Server**
  - Invia i messaggi associati alla chat
    ```json
    { "type": "message", "chatId": "...", "text": "...", "created_at": "...", "username": "...", "userId": "..." }
    ```

#### UTENTE CREA NUOVA CHAT CON UN ALTRO UTENTE
- **Client**
  - Invia l'username dell'utente con cui vuole creare la chat
    ```json
    { "type": "chatRequest", "name": "..." }
    ```
- **Server**
  - **Errore** (utente ricercato NON esiste o già presente nella lista delle chat)
    - Invia messaggio con errore
      ```json
      { "type": "chatResponseError", "message": "..." }
      ```
  - **Successo**
    - Invia a tutti e due gli utenti (il 2° solo se connesso) la chat
      ```json
      { "type": "chat", "id": "...", "name": "...", "photo": "..." }
      ```

#### UTENTE CREA NUOVO GRUPPO / VUOLE UNIRSI
- **Client**
  - Invia nome del gruppo
    ```json
    { "type": "groupRequest", "name": "..." }
    ```
- **Server**
  - **GRUPPO ESISTE**
    - Modifica la chat e la reinvia a tutti gli utenti del gruppo (solo quelli connessi)
      ```json
      { "type": "chat", "id": "...", "name": "...", "photo": "..." }
      ```
  - **GRUPPO NON ESISTE**
    - Crea un nuovo gruppo e lo invia al client
      ```json
      { "type": "chat", "id": "...", "name": "...", "photo": "..." }
      ```

### Dettaglio Chat
- **Client**
  - Invia un messaggio
    ```json
    { "type": "messageSent", "chatId": "...", "text": "...", "userId": "..." }
    ```
- **Server**
  - Invia messaggio a tutti i partecipanti della chat
    ```json
    { "type": "message", "chatId": "...", "text": "...", "created_at": "...", "username": "...", "userId": "..." }
    ```



<!-- Lista di chiamate possibili:                                                                                
- Login/Registrazione:
    Client:
        - inviamo username e password                                               { type:"loginRequest", username, password }
    Server:
        - Errore: 
            - indica l'errore avvenuto dentro la funzione di login/registrazione    { type:"loginError", message }
        - Successo:                                                                 
            - invia lo user appena registrato                                       { type:"loggedUser", id, username, profilePicture }
            - invia le chat associate allo user appena registrato.                  { type:"chat", id, name, photo, users },{},{},... // users can be null users = [{id,username},{id,username}]

- Homepage:
    - INTERAZIONE CON CHAT -> (utente clicca su una chat)
        Client: 
            - invia id_Chat al client                                               { type:"messagesRequest", idChat }
        Server: 
            - invia i messaggi associati alla chat                                  { type:"message", chatId, text, Date created_at, String username, int userId},{},{},... 

    - UTENTE CREA NUOVA CHAT CON UN'ALTRO UTENTE
        Client: 
            - invia l'username dell'utente con cui vuole creare la chat             { type:"chatRequest", name }
        Server:
            Errore: (utente ricercato NON esiste o già presente nella lista delle chat)
                - invia messaggio con errore                                        { type:"chatResponseError", message }
            Successo: 
                - invia a tutti e due gli utenti (il 2° solo se connesso) la chat.  { type:"chat", id, name, photo}

    - UTENTE CREA NUOVO GRUPPO / VUOLE UNIRSI
        Client: 
            - invia nome del gruppo                                                 { type:"groupRequest", name }
        Server:
            - GRUPPO ESISTE
                - modifica la chat e la reinvia a tutti gli utenti del gruppo (solo quelli connessi)  { type:"chat", id, name, photo }
            
            - GRUPPO NON ESISTE
                - crea un nuovo gruppo e lo invia al client                         { type:"chat", id, name, photo }

- Dettaglio Chat:
    Client:
        - invia un messaggio                                                        { type:"messageSent", chatId, text, int userId }
    Server
        - invia messaggio a tutti i partecipanti della chat                         { type:"message", chatId, text, Date created_at, String username, int userId} -->
