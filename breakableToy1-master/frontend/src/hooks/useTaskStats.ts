import { useState } from "react";
import { Task } from "../Types";
import { calculateAverages, formatDuration } from "../utils/taskUtils";

export function useTaskStats() {
  const [averages, setAverages] = useState({
    High: 0,
    Medium: 0,
    Low: 0,
    All: 0,
  });

  const updateStats = (tasks: Task[]) => {
    setAverages(calculateAverages(tasks));
  };

  return {
    updateStats,
    values: averages, // Números puros (en minutos)
    formatted: {
      averageHigh: formatDuration(averages.High),
      averageMedium: formatDuration(averages.Medium),
      averageLow: formatDuration(averages.Low),
      averageAll: formatDuration(averages.All),
    },
  };
}
