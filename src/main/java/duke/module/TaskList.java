package duke.module;

import duke.task.Task;
import duke.exception.DukeIllegalIndexException;
import duke.module.AutoResponse;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Keeps track of the all the user tasks.
 * When Duke application reboots, TaskList is loaded with previous tasks through {@link Storage}.
 */
public class TaskList {

    /** List storing all the user tasks. */
    private List<Task> taskList;

    /**
     * Constructs an empty ArrayList.
     * Default constructor for TaskList.
     */
    public TaskList() {
        this(new ArrayList<>());
    }

    /**
     * Loads the TaskList with the given list of tasks.
     *
     * @param taskList List of tasks to initialize TaskList.
     */
    public TaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    /**
     * Generates an en exception message depending on the given index.
     *
     * @param index Index of the TaskList.
     * @return An exception message.
     */
    private String generateIndexExceptionMessage(int index) {
        String message;
        if (index < 1) {
            message = AutoResponse.ERROR_NONPOSITIVE_INDEX;
        } else {
            message = String.format(AutoResponse.ERROR_EXCEED_INDEX, this.taskList.size());
        }
        return message;
    }

    /**
     * Marks the {@link Task} at given index as done.
     * <p>
     * <b>Prerequisite: </b>index starts from 1.
     *
     * @param index Index of the Task to mark as done.
     * @throws DukeIllegalIndexException When the given index is out of bounds.
     */
    public void markAsDoneTaskAt(int index) throws DukeIllegalIndexException {
        try {
            this.taskList.get(index - 1).markAsDone();
        } catch (IndexOutOfBoundsException e) {
            throw new DukeIllegalIndexException(this.generateIndexExceptionMessage(index));
        }
    }

    /**
     * Marks all tasks as done.
     */
    public void markAsDoneAllTasks() {
        this.taskList.forEach(Task::markAsDone);
    }

    /**
     * Adds given task to the back of the TaskList.
     *
     * @param task {@link Task} to be added.
     */
    public void addTask(Task task) {
        this.taskList.add(task);
    }

    /**
     * Returns the {@link Task} at given index.
     * <p>
     * <b>Prerequisite: </b>index starts from 1.
     *
     * @param index Index of the Task to return.
     * @return The Task at the index.
     * @throws DukeIllegalIndexException When the given index is out of bounds.
     */
    public Task getTaskAt(int index) throws DukeIllegalIndexException {
        try {
            return this.taskList.get(index - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new DukeIllegalIndexException(this.generateIndexExceptionMessage(index));
        }
    }

    public int getSize() {
        return this.taskList.size();
    }

    public boolean isEmpty() {
        return this.taskList.size() == 0;
    }

    /**
     * Deletes the task at the given index.
     *
     * @param index Index of the Task to delete.
     * @return Task to be deleted.
     * @throws DukeIllegalIndexException When the given index is out of bounds.
     */
    public Task deleteTaskAt(int index) throws DukeIllegalIndexException {
        try {
            return this.taskList.remove(index - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new DukeIllegalIndexException(this.generateIndexExceptionMessage(index));
        }
    }

    /**
     * Deletes all the tasks in this TaskList.
     */
    public void deleteAllTasks() {
        this.taskList.clear();
    }

    /**
     * Returns the list of all the {@link Task} objects whose description contains the given keyword.
     *
     * @param keyword Character sequence to match with the descriptions of Task objects.
     * @return List of Task objects containing the keyword.
     */
    public List<Task> findAllTasksContaining(String keyword) {
        return this.taskList.stream()
                            .filter(task -> task.getDescription().contains(keyword))
                            .collect(Collectors.toList());

    }

    /**
     * Returns the {@link Iterator} of this TaskList.
     *
     * @return The Iterator of this TaskList.
     */
    public Iterator<Task> iterator() {
        return this.taskList.iterator();
    }

    /**
     * Returns an array of Strings with each {@link Task} expressed in Strings.
     *
     * @return An array of Strings corresponding to {@link Task#toString()}.
     */
    public String[] listAll() {
        if (this.taskList.size() == 0) {
            return new String[] { AutoResponse.DUKE_NO_TASKS };
        }

        List<String> lines = new ArrayList<>();
        lines.add(AutoResponse.DUKE_LIST_TASKS);
        for (int i = 0; i < this.taskList.size(); i++) {
            lines.add(String.format("  %d.%s",
                    i + 1,
                    this.taskList.get(i).getStatus()));
        }
        return lines.toArray(new String[0]);
    }

}