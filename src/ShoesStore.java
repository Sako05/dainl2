import models.Customer;
import models.Shoe;

import java.util.*;
import java.util.stream.Collectors;

public class ShoesStore {

    private Repository repo;
    List<String> shoeIDcartList = new ArrayList<>();
    List<String> shoeCartList = new ArrayList<>();
    private int orderID = 0;

    public ShoesStore() {
        repo = new Repository();
        welcomeDialogue();

    }


    public void welcomeDialogue(){



        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to ShopforShoe \n Please press your desired option: \n (1)login | (2)register ");




        while (scan.hasNext()){
            String chosenOption = scan.next();
            if (chosenOption.equals("2")) {
                //makeNewCustomer();
            } else if (chosenOption.equals("1")){
                System.out.println("Skriv in ditt användarnamn:");
                String userID = scan.next();
                System.out.println("Skriv in ditt lösenord:");
                String password = scan.next();


                if(repo.login(userID, password) == null){
                    System.out.println("\nOgiltig inloggning, försök igen\n\n");
                    welcomeDialogue();
                    break;
                } else {
                    Customer customer = repo.login(userID, password);
                    int customerid = customer.getId();
                    System.out.println("Inloggning lyckades\n");
                    System.out.println("Välkommen "+ customer.getFirstName()+" "+customer.getLastName());

                    homeMenu(customerid);



                }

            }



        }

    }

    public void homeMenu(int customerid){
        Scanner scan = new Scanner(System.in);
        System.out.println("1. Beställ produkter");
        System.out.println("2. Beställningshistorik");
        System.out.println("3. Close session");

        String chosenOption2 = scan.next();

        switch (chosenOption2) {
            case "1":
                choseAShoe(customerid);
                break;
            case "2":
                showCart();

                System.out.println("\nTryck enter för att komma till hemmenyn...");
                new java.util.Scanner(System.in).nextLine();
                homeMenu(customerid);
                break;
            case "3":
                System.exit(0);
                break;
        }

    }

    public void choseAShoe(int customerid){
        System.out.println(getShoesWithOnlyQuantity());


        Scanner scan = new Scanner(System.in);
        System.out.println("Ange Skonamn, Storlek, Färg:");
        String shoes = scan.next();
        getShoeFromInput(shoes, customerid);

    }


    public void getShoeFromInput(String inputShoe, int customerid) {
        String[] shoeInput = inputShoe.split(",");
        if (shoeInput.length != 3) {
            System.out.println("Felaktig inmatning. [Skonamn],[storlek],[färg] | Tryck enter för att förska igen...");
            new java.util.Scanner(System.in).nextLine();
            choseAShoe(customerid);
        }
        else {
            if (!getShoeToString(shoeInput[0], shoeInput[1], shoeInput[2]).isEmpty())  {
                System.out.println("\n" + getShoeToString(shoeInput[0],
                        shoeInput[1],shoeInput[2])+"\n Skorna tillaga i varukorgen!\n");
                shoeIDcartList.add(getShoeIdByModelString(shoeInput[0],
                        shoeInput[1], shoeInput[2]));
                shoeCartList.add(getShoeToString(shoeInput[0],
                        shoeInput[1], shoeInput[2]));

                Scanner scan = new Scanner(System.in);
                System.out.println("Vill du lägga till ytterligare produkter eller bekräfta varukorgen?");
                System.out.println("(1) för att lägga till ytterligare produkter");
                System.out.println("(2) för att bekräfta varukorgen");
                System.out.println("(3) för att visa varukorgen");
                String chosenOption = scan.next();
                if (chosenOption.equals("1")) {
                    choseAShoe(customerid);
                } else if (chosenOption.equals("2")){
                    confirmOrder(customerid);
                } else if (chosenOption.equals("3")){
                    showCart();

                    System.out.println("(1) för att lägga till ytterligare produkter");
                    System.out.println("(2) för att bekräfta köpet");
                    System.out.println("(3) för komma till hemmenyn");
                    String chosenOption2 = scan.next();

                    if (chosenOption2.equals("1")) {
                        choseAShoe(customerid);
                    } else if (chosenOption2.equals("2")){
                        confirmOrder(customerid);
                    }else if (chosenOption2.equals("3")){
                        homeMenu(customerid);
                    }

                }


            } else {

                System.out.println("\nInmatad sko finns inte i sortiment, testa igen! | Tryck enter för att förska igen...");
                new java.util.Scanner(System.in).nextLine();
                choseAShoe(customerid);
            }
        }

    }




    public String getShoesWithOnlyQuantity(){
        Map<Integer, Shoe> shoesMap1 = repo.getShoes();

        return shoesMap1.values().stream()
                .filter(shoe -> shoe.getAmount() > 0)
                .map(shoe -> " " + shoe)
                .collect(Collectors.joining());
    }


    public String getShoeIdByModelString(String shoename, String size, String color) {
        Map<Integer, Shoe> shoesMap = repo.getShoes();

        return shoesMap.entrySet().stream()
                .filter(x -> shoename.equals(x.getValue().getshoeName()))
                .filter(x -> size.equals(x.getValue().getSize()))
                .filter(x -> color.equals(x.getValue().getColorName()))
                .map(x->x.getKey().toString())
                .collect(Collectors.joining());
    }

    public String getShoeToString(String shoename, String size, String color) {
        Map<Integer, Shoe> shoesMap = repo.getShoes();


        return shoesMap.entrySet().stream()
                .filter(x -> shoename.equals(x.getValue().getshoeName()))
                .filter(x -> size.equals(x.getValue().getSize()))
                .filter(x -> color.equals(x.getValue().getColorName()))
                .map(v -> " " + v.getValue())
                .collect(Collectors.joining());
    }


    public void confirmOrder(int customerID){
        if (!shoeIDcartList.isEmpty()) {
            for (String shoe : shoeIDcartList) {
                repo.addToCart(Integer.parseInt(shoe), customerID, orderID);
                orderID = getOrderIdFromDB();
            }
            showCart();
            System.out.println("\n  Order bekräftad ");;

        } else

            System.out.println("\nVarukorgen är tom!\nLägg skor i varukorgen först!");


    }


    public int getOrderIdFromDB() {
        return repo.onlyOrderId().get(repo.onlyOrderId().size()-1);
    }


    public void showCart() {
        if (!shoeCartList.isEmpty()) {
            System.out.println("   VARUKORG    \n");
            shoeCartList.forEach(e -> System.out.println(e));
        } else
            System.out.println("\n   VARUKORG TOM  \n");
    }




}
