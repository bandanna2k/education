package education.designpatterns.filterPattern.criteria;

import education.designpatterns.filterPattern.Criteria;
import education.designpatterns.filterPattern.Person;

import java.util.List;

public class AndCriteria implements Criteria
{

    private final Criteria criteria;
    private final Criteria otherCriteria;

    public AndCriteria(Criteria criteria, Criteria otherCriteria) {
        this.criteria = criteria;
        this.otherCriteria = otherCriteria;
    }

    @Override
    public List<Person> meetCriteria(List<Person> persons) {

        List<Person> firstCriteriaPersons = criteria.meetCriteria(persons);
        return otherCriteria.meetCriteria(firstCriteriaPersons);
    }
}