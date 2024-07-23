package example.withJson;

public record Event(String name, String description)
{
    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
