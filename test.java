public class test{
    //Properties
    Main main;

    //Methods
    public void testversioning(){
       System.out.println(this.main.blnHost); 
    }


    //Constructor
    public test(Main main){
        this.main = main;
        System.out.println(this.main.blnHost);
    }
}
