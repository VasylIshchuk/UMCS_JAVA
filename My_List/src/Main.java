public class Main {
    public static void main(String[] args) {
        CustomList<Employees> list = new CustomList<>();
        Employees waiter = new Waiter("Mark", 500);
        Employees cook = new Cook("Sanji", 1000);
        Employees waiter_2 = new Waiter("Jordan", 400);
        list.add(waiter);
        list.add(cook);
        list.add(waiter_2);
        CustomList<Employees> newList = CustomList.filterByClass(list, Employees.class);
        for (Employees employees : newList) {
            System.out.println(employees);
        }

//        CustomList<String> str = new CustomList<>();
//        str.addLast("what happened");
//        str.addLast(", happened");
//        str.addFirst( "Lad ");
//
//        //  iterator()
//        for(String value : str){
//            System.out.print(value);
//        }
//        System.out.println();
//
//        //  stream()
//        str.stream().skip(1).forEach(System.out::print);
//        System.out.println();
//
//        System.out.println(str.removeLast());
//        System.out.println(str.head.value);
//        System.out.println(str.head.next.value);
//        System.out.println(str.head.next.next.value);
//        System.out.println(str.get(2));
    }
}