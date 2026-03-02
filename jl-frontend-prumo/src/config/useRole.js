import { useEffect, useState } from "react";
import { supabase } from "config/supabase";
import { jwtDecode } from "jwt-decode";

export const useRole = () => {
  const [role, setRole] = useState<string | null>(null);

  useEffect(() => {
    const getRole = async () => {
      const { data } = await supabase.auth.getSession();
      const token = data.session?.access_token;

      if (token) {
        const decoded = jwtDecode(token);
        setRole(decoded.app_metadata?.role ?? null);
      }
    };

    getRole();
  }, []);

  return role;
};
