import { http } from "./http";

export type RegisterRequest = {
  email: string;
  password: string;
};

export type VerifyRequest = {
  email: string;
  otp: string;
};

export type AuthMessageResponse = {
  message: string;
};

export type LoginRequest = {
  email: string;
  password: string;
};

export type LoginResponse = {
  message: string;
  token: string;
};

export type MeResponse = {
  email: string;
  role: string;
};

//Reset Password
export type PasswordResetRequest = { 
  email: string 
};

export type PasswordResetVerifyRequest = { 
  email: string; 
  otp: string 
};
export type PasswordResetVerifyResponse = { 
  message: string; 
  resetToken: string };

export type PasswordResetConfirmRequest = { 
  email: string; 
  resetToken: string; 
  newPassword: string 
};

export async function registerUser(body: RegisterRequest): Promise<AuthMessageResponse> {
  const { data } = await http.post<AuthMessageResponse>("/auth/register", body);
  return data;
}

export async function verifyUser(body: VerifyRequest): Promise<AuthMessageResponse> {
  const { data } = await http.post<AuthMessageResponse>("/auth/verify", body);
  return data;
}

export async function login(body: LoginRequest): Promise<LoginResponse> {
  const { data } = await http.post<LoginResponse>("/auth/login", body);
  return data;
}

export async function me(): Promise<MeResponse> {
  const { data } = await http.get<MeResponse>("/me");
  return data;
}

//Reset Password
export async function requestPasswordReset(body: PasswordResetRequest) {
  const { data } = await http.post<AuthMessageResponse>("/auth/password-reset/request", body);
  return data;
}

export async function verifyPasswordResetOtp(body: PasswordResetVerifyRequest) {
  const { data } = await http.post<PasswordResetVerifyResponse>("/auth/password-reset/verify", body);
  return data;
}

export async function confirmPasswordReset(body: PasswordResetConfirmRequest) {
  const { data } = await http.post<AuthMessageResponse>("/auth/password-reset/confirm", body);
  return data;
}
