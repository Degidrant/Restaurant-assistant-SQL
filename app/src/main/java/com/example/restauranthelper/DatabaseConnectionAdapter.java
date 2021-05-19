package com.example.restauranthelper;

import android.content.Context;
import android.widget.Toast;

import com.example.restauranthelper.objects.CMessage;
import com.example.restauranthelper.objects.CBill;
import com.example.restauranthelper.objects.CDish;
import com.example.restauranthelper.objects.COption;
import com.example.restauranthelper.objects.COrder;
import com.example.restauranthelper.objects.COrderMain;
import com.example.restauranthelper.objects.CPerson;
import com.example.restauranthelper.objects.CTable;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DatabaseConnectionAdapter {
    public Connection connection;

    public DatabaseConnectionAdapter(@NotNull Connection connection)  {
        this.connection = connection;
    }

    //получить все блюда по группе
    public ArrayList<CDish> getDishesByGroup(@NotNull String group){
        ArrayList<CDish> dishes = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select ID, Name, DishType, Price, Weight from DishTable WHERE DishType = '" + group + "' Order By Name Asc;");
            while (resultSet.next()){
                CDish dish = new CDish(resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getInt(1), resultSet.getInt(5));
                dishes.add(dish);
            }
        }
        catch (Exception exception){
            return dishes;
        }
            return dishes;
    }

    //получить картинку блюда
    public String getMyBase(int id){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select Uri from DishTable WHERE ID = '" + id + "';");
            resultSet.next();
            return resultSet.getString(1);
        }
        catch (Exception exception){
            return "";
        }
    }

    //получить все варианты блюда по его ID
    public ArrayList<COption> getOptions(int dishID){
        ArrayList<COption> options = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from DishOptions WHERE DishID = " + dishID + " Order By OptionName Asc;");
            while (resultSet.next()){
                options.add(new COption(resultSet.getString(3), resultSet.getInt(1), resultSet.getInt(4)));
            }
        }
        catch (Exception exception){
            return null;
        }
        return options;
    }

    //получить ID родительского для опции блюда
    public int getOptionParentID(int id) {
        int did = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select DishId from DishOptions WHERE ID = " + id + ";");
            resultSet.next();
            did = resultSet.getInt(1);
        }
        catch (Exception exception){
            return did;
        }
        return did;
    }

    //создать заказ из листа заказа
    public void commitOrder(@NotNull ArrayList<COrder> order, String toString, int table, int personID, @NotNull Context context) {
        try {
            Statement statement = connection.createStatement();
            Date date = Calendar.getInstance().getTime();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            String datestring = sqlDate.toString().replaceAll("-", "");
            Time time = new Time(Calendar.getInstance().getTime().getTime());
            String timestring = time.toString();
            statement.executeQuery("Insert into OrderMainTable values ('" + datestring + "', '"
                    + timestring + "', '"+ toString + "', 0, " + table + "," + personID + ", NULL); ");
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
            int ID = getLastOrderID();

            for (COrder i : order){
                try {
                    Statement statement = connection.createStatement();
                    statement.executeQuery("Insert OrderSubsTable (OrderID, DishID, OptionID, Count) values (" + ID + ", " + i.getDishId() + ", " + i.getOptionId() + ", " + i.getCount() + ");");
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        Toast.makeText(context, "Заказ оформлен, sID = " + ID + ", Позиций: " + order.size() + "\nПроверьте и подтвердите заказ в меню заказов.", Toast.LENGTH_LONG).show();
    }

    //получить ID последнего заказа
    private int getLastOrderID() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select ID from OrderMainTable Order By ID Desc;");
            resultSet.next();
            return resultSet.getInt(1);
        }
        catch (Exception exception){
            return -1;
        }
    }

    //получить название блюда по ID
    public String getDishNameByID(int id){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select Name from DishTable WHERE ID = " + id + ";");
            resultSet.next();
            return resultSet.getString(1);
        }
        catch (Exception exception){
            return "";
        }
    }

    //получить название опции по ID
    public String getOptionNameByID(int id, @NotNull Context context){
        if (id == -1) return context.getString(R.string.withoutoptions);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select OptionName from DishOptions WHERE ID = " + id + ";");
            resultSet.next();
            return resultSet.getString(1);
        }
        catch (Exception exception){
            return context.getString(R.string.withoutoptions);
        }
    }

    //получить все невыполненые заказы
    public ArrayList<COrderMain> getOrders(){
        ArrayList<COrderMain> orders= new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from OrderMainTable WHERE Status < 4;");
            while (resultSet.next()){
                orders.add(new COrderMain(resultSet.getInt("ID"), resultSet.getTime("Time"), resultSet.getDate("Date"), resultSet.getInt("Status"),
                        resultSet.getInt("PersonaID"), resultSet.getInt("TableID"), resultSet.getString("Comment")));
            }
        }
        catch (Exception exception){
            exception.printStackTrace();

            return orders;
        }
        return orders;
    }

    //получить все невыполненные заказы по ID официанта
    public ArrayList<COrderMain> getOrders(int person){
        ArrayList<COrderMain> orders = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from OrderMainTable WHERE Status < 4 AND PersonaID = " + person + ";");
            while (resultSet.next()){
                orders.add(new COrderMain(resultSet.getInt("ID"), resultSet.getTime("Time"), resultSet.getDate("Date"), resultSet.getInt("Status"),
                        resultSet.getInt("PersonaID"), resultSet.getInt("TableID"), resultSet.getString("Comment")));
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return orders;
    }

    //утвердить следующий статус для заказа
    public void addOneStep(int id) {
        try {
            Statement statement = connection.createStatement();
           statement.executeQuery("Update OrderMainTable set Status = Status + 1 where ID = " + id + ";");
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }

    //Получить все блюда по ID заказа
    public ArrayList<COrder> getSubsOrders(int id) {
        ArrayList<COrder> subs= new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from OrderSubsTable WHERE OrderID = " + id + " ;");
            while (resultSet.next()){
                subs.add(new COrder(resultSet.getInt("DishID"), resultSet.getInt("OptionID"), resultSet.getInt("Count")));
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
            return subs;
        }
        return subs;
    }

    //проверить есть ли у столика #table заказы и вернуть true если их нет
    public boolean isOrderFinished(int table){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select ID from OrderMainTable WHERE Status < 3 AND TableID = " + table + " ;");
            return !resultSet.isBeforeFirst();
        }
        catch (Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    //утвердить чек
    public int assignBill(int tableID, int sum, int personID){
        int ID = 0;
        try {
            Statement statement = connection.createStatement();
            Date date = Calendar.getInstance().getTime();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            String datestring = sqlDate.toString().replaceAll("-", "");
            Time time = new Time(Calendar.getInstance().getTime().getTime());
            String timestring = time.toString();
            statement.executeQuery("Insert into BillTable values ('" + datestring + "', ' "+ timestring + "', "+ personID +", " + sum + "," + tableID + "); ");
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select ID from BillTable Order By ID Desc;");
            resultSet.next();
            ID = resultSet.getInt(1);
            statement.executeQuery("Update OrderMainTable set BillID = " + ID + " Where TableID = " + tableID + ";");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ID;
    }

    //получить строками номера всех выданных заказов столика №tableID
    public ArrayList<String> getTableOrders(int tableID){
        ArrayList<String> ids = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select ID from OrderMainTable WHERE Status = 3 AND TableID = " + tableID + " Order By ID Desc;");
            while (resultSet.next())
                ids.add(String.valueOf(resultSet.getInt(1)));
        }
        catch (Exception exception){
            exception.printStackTrace();
            return ids;
        }
        return ids;
    }

    //получить строками номера всех невыполненных заказов столика №tableID
    public ArrayList<String> getAllTableOrders(int tableID){
        ArrayList<String> ids = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select ID from OrderMainTable WHERE Status < 4 AND TableID = " + tableID + " ;");
            while (resultSet.next())
                ids.add(String.valueOf(resultSet.getInt(1)));
        }
        catch (Exception exception){
            exception.printStackTrace();
            return ids;
        }
        return ids;
    }

    //посчитать финальную сумму заказа по его ID
    public int countSum(int OrderId) {
        ArrayList<COrder> ordered= new ArrayList<>();
        int sum = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from OrderSubsTable WHERE OrderID = " + OrderId + " ;");
            while (resultSet.next())
                ordered.add(new COrder(resultSet.getInt("DishID"), resultSet.getInt("OptionID"), resultSet.getInt("Count")));
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        for (COrder i : ordered) {
            int subsum = 0;
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSetDish = statement.executeQuery("Select Price from DishTable Where ID = " + i.getDishId() + ";");
                resultSetDish.next();
                int price = resultSetDish.getInt(1);
                if (i.getOptionId() != -1) {
                    ResultSet resultSetOption = statement.executeQuery("Select OptionPrice from DishOptions Where ID = " + i.getOptionId() + ";");
                    resultSetOption.next();
                    subsum = (price + resultSetOption.getInt(1)) * i.getCount();
                }
                else subsum = price * i.getCount();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            sum += subsum;
        }
        return sum;
    }

    //возвращает строку ФИ работника
    public String getPersonString(int personID) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select Name, Surname from Users WHERE ID = " + personID + ";");
            resultSet.next();
            return resultSet.getString("Surname") + " " + resultSet.getString("Name");
        }
        catch (Exception exception){
            exception.printStackTrace();
            return "";
        }
    }

    //возвращает строками все заказы по ID чека
    public String getOrdersString(int id) {
        ArrayList<String> ids = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select ID from OrderMainTable WHERE BillID = " + id + " ;");
            while (resultSet.next())
                ids.add(String.valueOf(resultSet.getInt("ID")));
        }
        catch (Exception exception){
            exception.printStackTrace();
            return "";
        }
        StringBuilder orders = new StringBuilder("");
        for (String s : ids){
            orders.append(s);
            orders.append(", ");
        }
        try {
            orders.delete(orders.length() - 2, orders.length());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return orders.toString();
    }

    //возвращает 120 последних чеков
    public ArrayList<CBill> getBills() {
        ArrayList<CBill> bills = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select TOP (120) * from BillTable Order By ID Desc");
            while (resultSet.next())
                bills.add(new CBill(resultSet.getInt("ID"), resultSet.getInt("Sum"), resultSet.getInt("PersonID"), resultSet.getInt("TableID"), resultSet.getDate("Date"), resultSet.getTime("Time")));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bills;
    }

    //возвращает 60 последних чеков официанта id
    public ArrayList<CBill> getBills(int id) {
        ArrayList<CBill> bills = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select TOP (60) * from BillTable Where PersonID = "+ id + " Order By ID Desc");
            while (resultSet.next())
                bills.add(new CBill(resultSet.getInt("ID"), resultSet.getInt("Sum"), resultSet.getInt("PersonID"), resultSet.getInt("TableID"), resultSet.getDate("Date"), resultSet.getTime("Time")));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bills;
    }

    //возвращает статус работника
    public int getPersonStatus(int person) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select Status from Users WHERE ID = " + person + ";");
            resultSet.next();
            return resultSet.getInt(1);
        }
        catch (Exception exception){
            exception.printStackTrace();
            return -1;
        }
    }

    //возвращает количенство заказов либо по дате, либо всего (в случае null) по id работника
    public int getOrdersCount(java.sql.Date date, int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet;
            if (date!=null) resultSet = statement.executeQuery("Select ID from BillTable WHERE Date = '" + date.toString() + "' AND PersonID = " + id + ";");
            else resultSet = statement.executeQuery("Select ID from BillTable WHERE PersonID = " + id + ";");
            int a = 0;
            while (resultSet.next()) a++;
            return a;
        }
        catch (Exception exception){
            exception.printStackTrace();
            return 0;
        }
    }

    //получить константу процента от заказа в пользу официанта из БД
    public float getPersents(){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select Data from DataInt Where KeyV = 'Percent'");
            resultSet.next();
            return (float) (resultSet.getInt(1)/ 100.0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    //предварительно высчитать процент официанта либо по дате, либо всего (в случае null)
    public int getPredCount(java.sql.Date date, int id) {
        float mn = getPersents();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet;
            if (date!=null) resultSet = statement.executeQuery("Select Sum from BillTable WHERE Date = '" + date.toString() + "' AND PersonID = " + id + ";");
            else resultSet = statement.executeQuery("Select Sum from BillTable WHERE PersonID = " + id + ";");
            float a = 0;
            while (resultSet.next()) a+=resultSet.getInt(1)*mn;
            return (int) a;
        }
        catch (Exception exception){
            exception.printStackTrace();
            return 0;
        }
    }

    //получить рассчёт персонала по дате с несколькими вариантами для администратора и/или его подчинённых
    public ArrayList<CPerson> getPersonasData(@NotNull java.sql.Date date, int maxstatus) {
        ArrayList<CPerson> personaClasses= new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select Name, Surname, ID from Users Where Status >= 2 AND Status <= " + maxstatus + ";");
            while (resultSet.next())
                personaClasses.add(new CPerson(resultSet.getInt("ID"), (resultSet.getString("Surname") + " " + resultSet.getString("Name")), 0));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for (CPerson i : personaClasses) {
            try {
                Statement statement = connection.createStatement();
                float full = 0;
                float persent = getPersents();
                ResultSet resultSet = statement.executeQuery("Select Sum from BillTable Where PersonID = " + i.getID() + " AND Date = '" + date.toString() + "';");
                while (resultSet.next()) full += resultSet.getInt(1) * persent;
                i.setIncome((int) (full));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        personaClasses.sort((p1, p2) -> Integer.compare(p2.getIncome(), p1.getIncome()));
        return  personaClasses;
    }

    //получить константу количества столиков из БД
    public int howmuchtables() {
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select Data from DataInt Where KeyV = 'Tables'");
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    //возвращает лист объектов соответсвующих столикам в заведении, где характеристикой оных будет занятость
    public ArrayList<CTable> getTables(int count) {
        ArrayList<CTable> tables = new ArrayList<>();
        for (int i = 1; i <= count; i++)
            tables.add(new CTable(i, 0));
        for (CTable table : tables) {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("Select ID from OrderMainTable WHERE Status = 3 AND TableID = " + table.getID() + " ;");
                if (resultSet.next()) table.setStatus(2);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("Select ID from OrderMainTable WHERE Status < 3 AND TableID = " + table.getID() + " ;");
                if (resultSet.next()) table.setStatus(1);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return tables;
    }

    //получить num сообщений чата
    public ArrayList<CMessage> getMessages(int num){
        ArrayList<CMessage> messages = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select TOP (" + num + ") * from ChatTable " + "ORDER BY ID DESC;");
            while (resultSet.next()){
                messages.add(new CMessage(getPersonString(resultSet.getInt("PersonID")), resultSet.getString("Message"), resultSet.getDate("Date"), resultSet.getTime("Time")));
            }
        }
        catch (Exception exception){
            return messages;
        }
        return messages;
    }

    //отправить сообщение
    public void uploadmessage(@NotNull String message, int ID) {
        try{
            Statement statement = connection.createStatement();
            Date date = Calendar.getInstance().getTime();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            String datestring = sqlDate.toString().replaceAll("-", "");
            Time time = new Time(Calendar.getInstance().getTime().getTime());
            String timestring = time.toString();
            statement.executeQuery("Insert into ChatTable values (" + ID + ", '" + datestring  + "', '" + timestring + "', '" + message + "'); ");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
