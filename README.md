

the Mysql test 
![image](https://github.com/user-attachments/assets/4fe7c29a-3e0f-4be3-b699-306a17e0cb58)

Test de la couche service 
![image](https://github.com/user-attachments/assets/424a9e96-01b9-44d7-b637-fe18925c5ee8)


Add customer avec postman et la couche dto

![image](https://github.com/user-attachments/assets/8bc9eeda-70b9-46bd-96b2-6fdf3da20a95)

Update customer avec la couche service dto
![image](https://github.com/user-attachments/assets/4a224a65-403c-4320-9185-5a4518dab821)


il faut ajouter cette dépendance pour que swagger marche bien 
```
  <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.5.0</version>
        </dependency>

```

on accède d'ici 
```
http://localhost:8081/swagger-ui/index.html
```

![image](https://github.com/user-attachments/assets/544323c0-9090-448d-beac-e5b82e003041)

![image](https://github.com/user-attachments/assets/6e01acb3-9c27-4905-85f1-74663ae73edf)

test de API bankaccount avec swagger
![image](https://github.com/user-attachments/assets/6cd870f6-b28c-4ac5-b8c9-5bdcb9f90b8a)

après avoir modifier ça 
```
 public SavingAccountDTO fromSavingBankAccount(SavingAccount savingAccount) {
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        BeanUtils.copyProperties(savingAccount, savingAccountDTO);
        // parce que la propriètè customerdto qui existe dans savingaccountdto ne sera pas transfèrer du coup on a besoin de faire ceci
        savingAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        return savingAccountDTO;
    }
```
on ajoutant cette ligne 
```
        savingAccountDTO.setType(savingAccount.getClass().getSimpleName());
```
on peut voir le type de chaque banque
![image](https://github.com/user-attachments/assets/140d7acf-b61c-4177-9170-ad0d8a62cadb)

Voilà toutes les opérations il fallait ajouter l'annotation @Data pour qu'on puisse accèder ou setters et getters avec l'abscence de cette der,ière on ne peut pas avoir accès à tout les opérations d'un compte ou meme autre chose que opération
![image](https://github.com/user-attachments/assets/b5ef7ff1-edd8-4cc2-bdaf-3beaa164620f)

La pagination de la liste des opérations 
![image](https://github.com/user-attachments/assets/553361d0-a446-4cec-b17f-9c594b234b34)

Création du frontend 
on crée un dossier avec la commande 
```
ng new frontend
```
![image](https://github.com/user-attachments/assets/14f135c7-5647-47de-9c88-c442bae25650)

puis on test avec la commande suivante et fait le build
```
 ng serve
```
voilà il marche bien 
![image](https://github.com/user-attachments/assets/6e9fb364-32c4-419a-b761-b563c0b746e2)

installation des dépendances 

```
npm install bootstrap --save
npm install bootstrap-icons --save

```
pour ajouter le bootstrap à l'app on ajoute dans le style dans le fichier anglar.json
```

 "styles": [
              "node_modules/bootstrap-icons/font/bootstrap-icons.css",
              "src/styles.css",
              "node_modules/bootstrap/dist/css/bootstrap.min.css"
            ],
            "scripts": [
              "node_modules/bootstrap/dist/js/bootstrap.bundle.js"
            ]
```

création d'un component avec la commande 

```
 ng g c navbar
```

généralement on a un problème de CROS exemple on a utiliser ici dans l'app angular
```
ngOnInit():void{
      this.http.get("http://localhost:8081/customers").subscribe(data=>{ //une fois la réponse arrive par défaut il va stocker les données dans un object ts qui est data
        this.customers=data;

      },error => {
        console.log(error)
      })
  }
```
on doit autoriser 
on ajoute @CrossOrigin("*") pour tout controller dans la couche web

alors violà l'affichage des customers 
![image](https://github.com/user-attachments/assets/bff21857-c745-410f-a460-0d4f9c6ca31b)
sans oublier d'ajouter   NgIf,  NgForOf, dans impoert parcequ'on ne travaille pas avec l'ancienne version pas de app.module.ts 

pour les bonnes pratiques on va faire le traitement dans la couhce service et non sur le component
```
 ng g s services/customer
```

pour l'erreur qui dit 
```
core.mjs:6537 
 ERROR RuntimeError: NG0200: Circular dependency in DI detected for _CustomerService. Find more at https://angular.dev/errors/NG0200
    at NodeInjectorFactory.CustomersComponent_Factory [as factory] (customers.component.ts:18:32)

```

il faut que app.config.ts soit 
```
import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { HttpClientModule } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    importProvidersFrom(HttpClientModule)
  ]
};
```

recherche par keyword 
![image](https://github.com/user-attachments/assets/ca888f23-6c25-45af-a9ce-fa1b3f643384)

insertion d'un customer via interface graphique angular
![image](https://github.com/user-attachments/assets/8fa7d7ad-41f2-4f7b-a506-8941322de93c)

il faut faire attention par exemple on a fait dans le backend 
```
    @PostMapping("/addcustomer")
    public CustomerDTO addCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO);
    }
```

donc on doit prendre le meme url pour le frontend pour ne pas y avoir un problème de CORS malfrè qu'on a fait le mot clè @CrossOrigin("*") dans la couche web 
```
  public saveCustomers(customer : Customer):Observable <Customer>{
    return this.http.post<Customer>(this.baseUrl+"/addcustomer",customer)
  }
```

la sécurité il faut ajouter la dépendances suivantes 
```
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
        </dependency>
```

  
ng g interceptor interceptors/app-http

il faut ajouter à app.config 
```
import { appHttpInterceptor } from './interceptors/app-http.interceptor';
import {ApplicationConfig} from "@angular/core";
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { HttpClientModule } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([appHttpInterceptor])
    )
  ]
};
```
protèger les routes
```
ng g g guards/authentication

```

il faut installer cette dépendance pour stocker les roles et tout 
```
npm i jwt-decode
```
