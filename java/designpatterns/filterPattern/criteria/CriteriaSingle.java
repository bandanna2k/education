package education.designpatterns.filterPattern.criteria;

import education.designpatterns.filterPattern.Criteria;
import education.designpatterns.filterPattern.Person;

import java.util.ArrayList;
import java.util.List;

public class CriteriaSingle implements Criteria
{

    @Override
    public List<Person> meetCriteria(List<Person> persons) {
        List<Person> singlePersons = new ArrayList<Person>();

        for (Person person : persons) {
            if(person.getMaritalStatus().equalsIgnoreCase("SINGLE")){
                singlePersons.add(person);
            }
        }
        return singlePersons;
    }
}