import { fetchTasks } from "./TaskService";

beforeEach(() => {
    window.alert = jest.fn();
  
    global.fetch = jest.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([{ id: 1, name: "Test Task" }]),
      })
    ) as jest.Mock;
  });
  

describe("TaskService - fetchTasks", () => {
  it("calls the correct URL with page and no filters", async () => {
    await fetchTasks(1);
    expect(global.fetch).toHaveBeenCalledWith("/todos?page=1");
  });

  it("includes order and filters in the URL", async () => {
    await fetchTasks(2, "DueDateAsc", "priority=High");
    expect(global.fetch).toHaveBeenCalledWith("/todos?order=DueDateAsc&priority=High&page=2");
  });

  it("returns parsed JSON data", async () => {
    const data = await fetchTasks(0);
    expect(data).toEqual([{ id: 1, name: "Test Task" }]);
  });
});

