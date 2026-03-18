import { http } from "./http";

export type Specialty = {
  id: number;
  name: string;
  active: boolean;
};

export async function getSpecialties(): Promise<Specialty[]> {
  const { data } = await http.get<Specialty[]>("/specialties");
  return data;
}