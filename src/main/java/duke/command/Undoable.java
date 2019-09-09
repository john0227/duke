package duke.command;

import duke.exception.DukeException;

import duke.module.Storage;
import duke.module.TaskList;
import duke.module.Ui;

public interface Undoable {

    void undo(TaskList taskList, Ui ui, Storage storage) throws DukeException;

}
