describe("Task Flow End-to-End", () => {
  const taskName = "Tarea Cypress";
  const editedTask = "Tarea Cypress Editada";

  function findTaskAcrossPages(taskText: string, callback: () => void) {
    cy.get("body").then(($body) => {
      if ($body.find(".task-table").length && $body.find(".task-table").text().includes(taskText)) {
        callback();
      } else if ($body.find("button:contains('Next')").not(":disabled").length) {
        cy.contains("Next").click({ force: true });
        cy.wait("@getTodos");
        findTaskAcrossPages(taskText, callback);
      } else {
        throw new Error(`No se encontró la tarea "${taskText}" en ninguna página`);
      }
    });
  }

  beforeEach(() => {
    cy.intercept("GET", "/todos**").as("getTodos");
    cy.intercept("POST", "/todos").as("createTodo");
    cy.intercept("PUT", "/todos/**").as("updateTodo");
    cy.intercept("DELETE", "/todos/**").as("deleteTodo");
    cy.visit("http://localhost:8080");
    cy.wait("@getTodos");
  });

  it("should create a task", () => {
    cy.contains("+ Add New Task").click();
    cy.get('input[name="name"]').type(taskName);
    cy.get('input[name="dueDate"]').type("2025-05-01");
    cy.get('select[name="priority"]').select("High");
    cy.contains("Create task").click();
    cy.wait("@createTodo");
    cy.wait("@getTodos");
    findTaskAcrossPages(taskName, () => {
      cy.contains(taskName).should("exist");
    });
  });

  it("should filter by name", () => {
    cy.get('input#name').clear().type("Cypress");
    cy.contains("Search").click();
    cy.wait("@getTodos");
    cy.contains(taskName).should("exist");
  });

  it("should filter by priority", () => {
    cy.get('select#priority').select("High");
    cy.contains("Search").click();
    cy.wait("@getTodos");
    cy.contains(taskName).should("exist");
  });

  it("should filter by status undone", () => {
    cy.get('select#status').select("Undone");
    cy.contains("Search").click();
    cy.wait("@getTodos");
    cy.contains(taskName).should("exist");
  });

  it("should clear filters", () => {
    cy.contains("Clean").click();
    cy.contains("Search").click();
    cy.wait("@getTodos");
    findTaskAcrossPages(taskName, () => {
      cy.contains(taskName).should("exist");
    });
  });



  it("should verify averages show correctly", () => {
    cy.contains("Overall average:").should("exist");
    cy.contains("High Priority:").should("exist");
  });
});
