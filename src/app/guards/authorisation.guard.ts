import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authorisationGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Vérifier si l'utilisateur a le rôle ADMIN
  if (authService.roles && authService.roles.includes("ADMIN")) {
    return true; // Autoriser l'accès à la route
  } else {
    // Rediriger vers la page non autorisée
    router.navigateByUrl("/admin/notAuthorized");
    return false; // Bloquer l'accès à la route demandée
  }
};
