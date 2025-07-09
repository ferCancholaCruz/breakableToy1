import { Task } from "../Types";

export function calculateAverages(tasks: Task[]) {
  const groups = { High: [], Medium: [], Low: [] } as Record<string, number[]>;

  tasks.forEach((t) => {
    if (t.done && t.creationDate && t.doneDate) {
      const diff = new Date(t.doneDate).getTime() - new Date(t.creationDate).getTime();
      const mins = Math.ceil(diff / (1000 * 60));
      if (groups[t.priority]) groups[t.priority].push(mins);
    }
  });

  const avg = (arr: number[]) => (arr.length ? arr.reduce((a, b) => a + b, 0) / arr.length : 0);
  const all = [...groups.High, ...groups.Medium, ...groups.Low];
  return {
    High: avg(groups.High),
    Medium: avg(groups.Medium),
    Low: avg(groups.Low),
    All: avg(all),
  };
}

export function formatDuration(minutes: number): string {
  const total = Math.round(minutes);
  const days = Math.floor(total / 1440);
  const hours = Math.floor((total % 1440) / 60);
  const mins = total % 60;
  return [days && `${days}d`, hours && `${hours}h`, (mins || (!days && !hours)) && `${mins}m`]
    .filter(Boolean)
    .join(" ");
}
