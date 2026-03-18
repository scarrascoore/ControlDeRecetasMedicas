import { http } from "./http";

export type CreatePrescriptionRequest = {
  specialtyId: number;
  title: string;
  notes?: string | null;
};

export type PrescriptionResponse = {
  id: number;
  specialtyId: number;
  title: string;
  notes: string | null;
  createdAt: string;
};

export async function createPrescription(
  body: CreatePrescriptionRequest
): Promise<PrescriptionResponse> {
  const { data } = await http.post<PrescriptionResponse>("/prescriptions", body);
  return data;
}

export async function getPrescriptions(): Promise<PrescriptionResponse[]> {
  const { data } = await http.get<PrescriptionResponse[]>("/prescriptions/mine");
  return data;
}

export async function getPrescriptionById(id: number): Promise<PrescriptionResponse> {
  const { data } = await http.get<PrescriptionResponse>(`/prescriptions/${id}`);
  return data;
}