package tools;

import commands.Command;
import exceptions.XmlReadingException;
import exceptions.XmlSaveException;

import java.util.ArrayList;

/**
 * Класс, управляющий журналом команд.
 */
public class JournalManager {

    /**
     * Журнал команд.
     */
    public CommandList journal;

    /**
     * Создание экземпляра {@code JournalManager}
     */
    public JournalManager(){journal = new CommandList();}

    /**
     * Добавляет команду в журнал.
     * @param command добавляемая команда.
     */
    public void addCommand(Command command){
        journal.getCommands().add(command);
    }

    /**
     * Очищает журнал.
     */
    public void clearJournal(){
        journal.getCommands().clear();
    }

    /**
     * Записывает журнал в файл.
     */
    public void saveJournal(){
        try {
            XMLWriter xmlWriter = new XMLWriter();
            xmlWriter.journalToXML(journal);
        }catch (XmlSaveException e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Читает журнал из файла.
     */
    public void readJournal(){
        try {
            XMLReader xmlReader = new XMLReader();
            journal = xmlReader.readXmlJournal("journal.xml");
        } catch (XmlReadingException e) {
            System.out.println(e.getMessage());
        }
    }

    public CommandList getJournal(){
        return journal;
    }

}
