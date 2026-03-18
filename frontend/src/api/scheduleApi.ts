import { http } from "./http";

export type FrequencyType = "DAILY";

export type CreateScheduleRequest = {
  medicineName: string;
  dose: string;
  frequencyType: "DAILY";
  timesPerDay: number;
  intervalHours?: null;
  startDate: string;
  endDate: string;
  active: boolean;
  startTime: string;
};

export type ScheduleResponse = {
  id: number;
  prescriptionId: number;
  medicineName: string;
  dose: string;
  frequencyType: FrequencyType;
  timesPerDay: number | null;
  intervalHours: number | null;
  startDate: string;
  endDate: string;
  active: boolean;
  startTime: string;
};

export async function createSchedule(
  prescriptionId: number,
  body: CreateScheduleRequest
): Promise<ScheduleResponse> {
  const { data } = await http.post<ScheduleResponse>(
    `/prescriptions/${prescriptionId}/schedules`,
    body
  );
  return data;
}

export async function getSchedulesByPrescription(
  prescriptionId: number
): Promise<ScheduleResponse[]> {
  const { data } = await http.get<ScheduleResponse[]>(
    `/prescriptions/${prescriptionId}/schedules`
  );
  return data;
}