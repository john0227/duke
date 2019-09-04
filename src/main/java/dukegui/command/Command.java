package dukegui.command;

import duke.exception.DukeException;
import duke.module.Storage;
import duke.module.TaskList;

/**
 * Abstract class to represent the commands supported by Duke.
 */
public abstract class Command {

    /**
     * Returns the result of executing a certain function.
     *
     * @param taskList List of tasks to manage.
     * @param storage Storage to save any changes if necessary.
     * @return Result of executing this {@code Command}.
     * @throws DukeException When applicable.
     */
    public abstract String getResponse(TaskList taskList, Storage storage) throws DukeException;

    /**
     * Shows whether executing this Command should quit the Duke or not.
     *
     * @return True if Duke should quit, false otherwise.
     */
    public abstract boolean isExit();

}