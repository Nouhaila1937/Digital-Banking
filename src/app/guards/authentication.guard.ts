import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
export const authenticationGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Vérifier si l'utilisateur est authentifié
  if (authService.isAuthenticated) {
    console.log(authService.username+" est connecté "+authService.roles+" :son role")
    return true; // Autoriser l'accès à la route
  } else {
    // Rediriger vers la page de connexion
    router.navigateByUrl('/login');
    return false; // Bloquer l'accès à la route demandée
  }
};
