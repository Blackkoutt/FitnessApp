# Uruchomienie projektu

**1. Instalacja  Node.js i npm**

   ```
    https://nodejs.org/en
   ```
**2. Instalacja frameworku Express**

Otwierając katalog "rest" w terminalu wykonaj polecenie:
   ```
    npm install express
   ```
**3. Uruchomienie serwera**

Otwierając katalog "rest" w terminalu wykonaj polecenie:
   ```
    node server.js
   ```
> [!NOTE]  
> Serwer defaultowo uruchamia się na porcie 3000 http://localhost:3000.
> 
> Udostępnia on endpointy: GET /posts/:id, GET /posts/:id/comments, PUT /posts/:id/comments.

**4. Uruchomienie aplikacji**

Po uruchomieniu serwera server.js można uruchomić aplikację poprzez Android Studio.

> [!TIP]
> W przypadku usunięcia folderu node_modules można go zainstalować za pomocą polecenia: npm init -y.
