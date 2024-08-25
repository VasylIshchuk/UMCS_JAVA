public class Cook extends Employees {
    public String name;
    public int salary;

    public Cook(String name, int monthSalary) {
        this.name = name;
        salary(monthSalary);
    }

    @Override
    public void salary(int mouthSalary) {
        this.salary = mouthSalary * 12;
    }

    @Override
    public String toString() {
        return "Cook{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }
}
