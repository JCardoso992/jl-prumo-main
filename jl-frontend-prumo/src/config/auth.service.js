import { supabase } from "./supabase";
import { jwtDecode } from "jwt-decode";

class AuthService {
  /**
   * Login
   */
  static async login(email, password) {
    const { data, error } = await supabase.auth.signInWithPassword({
      email,
      password,
    });

    if (error) throw error;

    return data;
  }

  /**
   * Logout
   */
  static async logout() {
    await supabase.auth.signOut();
    window.location.href = "/login";
  }

  /**
   * Retorna sessão atual
   */
  static async getSession() {
    const { data } = await supabase.auth.getSession();
    return data.session;
  }

  /**
   * Retorna token atual (SÍNCRONO)
   */
  static getAccessTokenSync() {
    const session = supabase.auth.getSession();
    return null; // Não funciona síncrono aqui
  }

  /**
   * Retorna role (AGORA SÍNCRONO)
   */
  static getRoleFromToken(token){
    if (!token) return null;

    const decoded = jwtDecode<SupabaseJwtPayload>(token);
    return decoded.app_metadata?.role ?? null;
  }

  /**
   * Verifica role
   */
  static async hasRole(role) {
    const session = await this.getSession();
    const token = session?.access_token;

    const userRole = this.getRoleFromToken(token ?? null);

    return userRole === role;
  }

  /**
   * Verifica múltiplas roles
   */
  static async hasAnyRole(roles){
    const session = await this.getSession();
    const token = session?.access_token;

    const userRole = this.getRoleFromToken(token ?? null);

    if (!userRole) return false;

    return roles.includes(userRole);
  }
}

export default AuthService;
