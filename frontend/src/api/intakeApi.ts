import { http } from "./http";

export type TodayDoseStatus = "PENDING" | "TAKEN" | "SKIPPED";

export type TodayDose = {
  scheduleId: number;
  medicineName: string;
  dose: string;
  plannedTime: string;
  plannedAt: string;
  status: TodayDoseStatus;
  loggedAt: string | null;
  note: string | null;
  taken: boolean;
};

export async function getToday(): Promise<TodayDose[]> {
  const { data } = await http.get<TodayDose[]>("/today");
  return data;
}

export type MarkIntakeRequest = {
  plannedTime: string;
  status: Extract<TodayDoseStatus, "TAKEN" | "SKIPPED">;
  note?: string | null;
};

function toHHmm(time: string): string {
  return time.length >= 5 ? time.slice(0, 5) : time;
}

export async function markIntake(
  scheduleId: number,
  body: MarkIntakeRequest
): Promise<void> {
  const payload: MarkIntakeRequest = {
    ...body,
    plannedTime: toHHmm(body.plannedTime),
    note: body.note ?? null,
  };

  await http.post(`/schedules/${scheduleId}/intakes`, payload);
}