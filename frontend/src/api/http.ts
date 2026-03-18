import axios from "axios";

const baseURL = import.meta.env.VITE_API_URL;

export const http = axios.create({ baseURL });

http.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {

    config.headers?.set?.("Authorization", `Bearer ${token}`);

    if (!config.headers?.set) {
      config.headers = new axios.AxiosHeaders({
        Authorization: `Bearer ${token}`,
      });
    }
  }
  return config;
});
