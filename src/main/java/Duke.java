import java.util.Scanner;

class Duke {

    private static final String DUKE_HELLO = "Hello! I'm Duke\n    What can I do for you?\n";
    private static final String DUKE_BYE = "Bye. Hope to see you again soon!\n";
    private static final String DUKE_LIST_TASKS = "Here are the tasks in your list:";
    private static final String DUKE_MARK_AS_DONE = "Nice! I've marked this task as done:\n";
    private static final String DUKE_ADD_TASK = "Got it. I've added this task:\n";
    private static final String DUKE_DELETE_TASK = "Noted. I've removed this task:\n";
    private static final String DUKE_NUMBER_OF_TASKS = "Now you have %d tasks in the list.";
    private static final String DUKE_LINE = "    ____________________________________________________________\n";
    private static final String DUKE_TAB4 = "    ";
    private static final String DUKE_TAB2 = "  ";

    private static final String ERROR_MISSING_INDEX = "☹ OOPS!!! Please include the index of the task.";
    private static final String ERROR_ILLEGAL_INDEX = "☹ OOPS!!! The index must be a number "
            + "separated by one whitespace.";
    private static final String ERROR_MISSING_TASK_DESCRIPTION = "☹ OOPS!!! The description of a task "
            + "cannot be empty.";
    private static final String ERROR_MISSING_DESCRIPTION_AND_DATE = "☹ OOPS!!! Description and dates of a task "
            + "cannot be empty.";
    private static final String ERROR_MISSING_DEADLINE_DATE = "☹ OOPS!!! Deadline dates must be "
            + "specified after \"/by.\"";
    private static final String ERROR_MISSING_EVENT_DATE = "☹ OOPS!!! Deadline dates must be specified after \"/at.\"";
    private static final String ERROR_ILLEGAL_COMMAND = "☹ OOPS!!! I'm sorry, but I don't know what that means :-(";

    private static final String DELIMITER_DEADLINE_DATE = "/by";
    private static final String DELIMITER_EVENT_DATE = "/at";

    private TaskList taskList;

    private enum Command {
        LIST,
        DONE,
        TODO,
        EVENT,
        DEADLINE,
        DELETE;
    }

    Duke() {
        this.taskList = new TaskList();
    }

    private void listTasks() throws DukeIllegalIndexException {
        System.out.println(DUKE_TAB4 + DUKE_LIST_TASKS);
        for (int i = 1; i <= this.taskList.getSize(); i++) {
            System.out.println(String.format("%s%d.%s",
                                             DUKE_TAB4,
                                             i,
                                             taskList.getTaskAt(i).getStatus()));
        }
    }

    private void finishTask(String[] command) throws DukeIllegalIndexException, DukeIllegalArgumentException {
        try {
            int index = Integer.parseInt(command[1]);
            taskList.markAsDoneTaskAt(index);
            System.out.println(DUKE_TAB4 + DUKE_MARK_AS_DONE
                    + DUKE_TAB4 + DUKE_TAB2
                    + taskList.getTaskAt(index).getStatus());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DukeIllegalArgumentException(ERROR_MISSING_INDEX);
        } catch (NumberFormatException e) {
            throw new DukeIllegalIndexException(ERROR_ILLEGAL_INDEX);
        }
    }

    private void deleteTask(String[] command) throws DukeIllegalIndexException, DukeIllegalArgumentException {
        try {
            int index = Integer.parseInt(command[1]);
            System.out.println(DUKE_TAB4 + DUKE_DELETE_TASK
                    + DUKE_TAB4 + DUKE_TAB2 + taskList.deleteTaskAt(index).getStatus()
                    + String.format("\n" + DUKE_TAB4 + DUKE_NUMBER_OF_TASKS, taskList.getSize()));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DukeIllegalArgumentException(ERROR_MISSING_INDEX);
        } catch (NumberFormatException e) {
            throw new DukeIllegalIndexException(ERROR_ILLEGAL_INDEX);
        }
    }

    private void addTodoTask(String[] command) throws DukeIllegalArgumentException{
        if (command.length == 1) {
            throw new DukeIllegalArgumentException(ERROR_MISSING_TASK_DESCRIPTION);
        }

        StringBuilder task = new StringBuilder();
        for (int i = 1; i < command.length; i++) {
            task.append(command[i]).append(" ");
        }
        this.addTask(new TodoTask(task.toString().trim()));
    }

    private void addDeadlineTask(String[] command) throws DukeIllegalArgumentException {
        StringBuilder[] task = new StringBuilder[2];
        task[0] = new StringBuilder();
        task[1] = new StringBuilder();
        int index = 0;
        boolean hasDescription = false;
        boolean hasDeadlineDate = false;
        for (int i = 1; i < command.length; i++) {
            if (!hasDeadlineDate && command[i].equals(DELIMITER_DEADLINE_DATE)) {
                if (i != command.length - 1) {
                    hasDeadlineDate = true;
                    index++;
                    continue;
                }
            } else if (!hasDescription && !hasDeadlineDate && !command[i].equals("")) {
                hasDescription = true;
            }
            task[index].append(command[i]).append(" ");
        }
        if (!hasDescription && !hasDeadlineDate) {
            throw new DukeIllegalArgumentException(ERROR_MISSING_DESCRIPTION_AND_DATE);
        } else if (!hasDescription) {
            throw new DukeIllegalArgumentException(ERROR_MISSING_TASK_DESCRIPTION);
        } else if (!hasDeadlineDate) {
            throw new DukeIllegalArgumentException(ERROR_MISSING_DEADLINE_DATE);
        }
        this.addTask(new DeadlineTask(task[0].toString().trim(),
                                      task[1].toString().trim()));
    }

    private void addEventTask(String[] command) throws DukeIllegalArgumentException {
        StringBuilder[] task = new StringBuilder[2];
        task[0] = new StringBuilder();
        task[1] = new StringBuilder();
        int index = 0;
        boolean hasDescription = false;
        boolean hasEventDate = false;
        for (int i = 1; i < command.length; i++) {
            if (!hasEventDate && command[i].equals(DELIMITER_EVENT_DATE)) {
                if (i != command.length - 1) {
                    hasEventDate = true;
                    index++;
                    continue;
                }
            } else if (!hasDescription && !hasEventDate && !command[i].equals("")) {
                hasDescription = true;
            }
            task[index].append(command[i] + " ");
        }
        if (!hasDescription && !hasEventDate) {
            throw new DukeIllegalArgumentException(ERROR_MISSING_DESCRIPTION_AND_DATE);
        } else if (!hasDescription) {
            throw new DukeIllegalArgumentException(ERROR_MISSING_TASK_DESCRIPTION);
        } else if (!hasEventDate) {
            throw new DukeIllegalArgumentException(ERROR_MISSING_EVENT_DATE);
        }
        this.addTask(new EventTask(task[0].toString().trim(),
                                   task[1].toString().trim()));
    }

    private void addTask(Task task) {
        taskList.addTask(task);
        System.out.println(DUKE_TAB4 + DUKE_ADD_TASK
                + DUKE_TAB4 + DUKE_TAB2 + task.getStatus()
                + String.format("\n" + DUKE_TAB4 + DUKE_NUMBER_OF_TASKS,
                taskList.getSize()));
    }

    void run() {
//        String logo = " ____        _        \n"
//                + "|  _ \\ _   _| | _____ \n"
//                + "| | | | | | | |/ / _ \\\n"
//                + "| |_| | |_| |   <  __/\n"
//                + "|____/ \\__,_|_|\\_\\___|\n";
//        System.out.println("Hello from\n" + logo);
        Scanner sc = new Scanner(System.in);

        // Greet the user
        System.out.println(DUKE_LINE
                           + DUKE_TAB4 + DUKE_HELLO
                           + DUKE_LINE);

        // Handle user input
        String input;
        while (!(input = sc.nextLine()).equals("bye")) {
            try {
                System.out.print(DUKE_LINE);
                String[] command = input.split(" ");
                switch (Command.valueOf(command[0].toUpperCase())) {
                case LIST:
                    this.listTasks();
                    break;
                case DONE:
                    this.finishTask(command);
                    break;
                case DELETE:
                    this.deleteTask(command);
                    break;
                case TODO:
                    this.addTodoTask(command);
                    break;
                case DEADLINE:
                    this.addDeadlineTask(command);
                    break;
                case EVENT:
                    this.addEventTask(command);
                    break;
                }
            } catch (DukeIllegalIndexException | DukeIllegalArgumentException e) {
                System.out.println(DUKE_TAB4 + e);
            } catch (IllegalArgumentException e) {
                System.out.println(DUKE_TAB4 + ERROR_ILLEGAL_COMMAND);
            } finally {
                System.out.println(DUKE_LINE);
            }
        }

        // Greet the user and quit program
        System.out.print(DUKE_LINE
                         + DUKE_TAB4 + DUKE_BYE
                         + DUKE_LINE);
    }
    
}