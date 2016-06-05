/*
class is called when any day is clicked on
 */
package mypi.eventForm;

import mypi.IO.WriteToFile;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import mypi.service.EventLinkedList;
import mypi.pojo.Events;

/**
 *
 * @author trav
 */
public class CreateEventForm extends JPanel {

    private JLabel monthLabel, dayLabel, yearLabel;
    private final Box eventBox;
    private final JButton okay, cancel;
    private final JComboBox billsBox;
    private final String[] bills = {"Comcast", "Electric", "Gas", "Phone", "Insurance", "Other"};
    private final String[] quickPaymentAmount = {"$20", "$80", "$54.99", "$104.46", "$120", "$122.96", "$140", "$160", "Other"};
    private final JRadioButton paymentsButton[] = new JRadioButton[quickPaymentAmount.length];
    private final JPanel descriptionPanel;
    private JTextField descriptionTitle;
    private final JTextField manualInput = new JTextField(6);
    private Box horizontal5;//used for setting up manualInput text box in approprite location
    private final JTabbedPane theAppTabs;
    private Events pojo;

    /**
     * 
     * @param pojo
     * @param theAppTabs is called to control the tabs in CalendarApp
    */
    
    public CreateEventForm(Events pojo,JTabbedPane theAppTabs){
        this.theAppTabs = theAppTabs;
        this.pojo = pojo;
        OkayCancelListener okayCancelChanger = new OkayCancelListener();
        ComboBoxListener comboChanger = new ComboBoxListener();
        eventBox = Box.createVerticalBox();
        //provide the label of the current day of the event
        JPanel topPanel = new JPanel();
        monthLabel = new JLabel( pojo.getMonth() );
        dayLabel = new JLabel(pojo.getDay());
        yearLabel = new JLabel(pojo.getYear());
        topPanel.add(monthLabel);
        topPanel.add(dayLabel);
        topPanel.add(yearLabel);

        JPanel secondLayer = new JPanel();
        JLabel questionToUser = new JLabel("What Would You Like To Plan?");
        secondLayer.add(questionToUser);

        JPanel thirdLayer = new JPanel();
        billsBox = new JComboBox(bills);
        billsBox.addActionListener(comboChanger);
        thirdLayer.add(billsBox);

        descriptionPanel = new JPanel();
        displayRadioButton();

        JPanel decisionPanel = new JPanel();
        okay = new JButton("Okay");
        cancel = new JButton("Cancel");
        okay.addActionListener(okayCancelChanger);
        cancel.addActionListener(okayCancelChanger);
        decisionPanel.add(cancel);
        decisionPanel.add(okay);

        eventBox.add(topPanel);
        eventBox.add(secondLayer);
        eventBox.add(thirdLayer);
        eventBox.add(descriptionPanel);
        eventBox.add(decisionPanel);

        this.add(eventBox);
        this.setVisible(true);

    }



    /**
     * method is used if Comcast , Electric ,gas, phone or insurance is selected
     */
    private void displayRadioButton() {
        ButtonGroup quickPaymentGroup = new ButtonGroup();
        RadioButtonListener radioGroupListener = new RadioButtonListener();
        Box vertical = Box.createVerticalBox();
        Box horizontal = Box.createHorizontalBox();
        Box horizontal2 = Box.createHorizontalBox();
        Box horizontal3 = Box.createHorizontalBox();
        Box horizontal4 = Box.createHorizontalBox();
        horizontal5 = Box.createHorizontalBox();
        for (int i = 0; i < paymentsButton.length; i++) {
            if (i < 2) {
                quickPaymentGroup.add(paymentsButton[i] = new JRadioButton(quickPaymentAmount[i]));
                paymentsButton[i].addActionListener(radioGroupListener);
                horizontal.add(paymentsButton[i]);
            } else if (i >= 2 && i < 4) {
                quickPaymentGroup.add(paymentsButton[i] = new JRadioButton(quickPaymentAmount[i]));
                paymentsButton[i].addActionListener(radioGroupListener);
                horizontal2.add(paymentsButton[i]);
            } else if (i >= 4 && i < 6) {
                quickPaymentGroup.add(paymentsButton[i] = new JRadioButton(quickPaymentAmount[i]));
                paymentsButton[i].addActionListener(radioGroupListener);
                horizontal3.add(paymentsButton[i]);
            } else {
                quickPaymentGroup.add(paymentsButton[i] = new JRadioButton(quickPaymentAmount[i]));
                paymentsButton[i].addActionListener(radioGroupListener);
                horizontal4.add(paymentsButton[i]);
            }
        }//end for loop
        vertical.add(horizontal);
        vertical.add(horizontal2);
        vertical.add(horizontal3);
        vertical.add(horizontal4);
        vertical.add(horizontal5);
        descriptionPanel.add(vertical);
        descriptionPanel.updateUI();
    }

    private class RadioButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            AbstractButton radioButton = (AbstractButton) e.getSource();

            if (radioButton.getText().equals("Other")) {
                //manualInput = new JTextField(6);
                horizontal5.add(manualInput);
            } else {
                horizontal5.remove(manualInput);
            }
            updateUI();

        }
    }

    private class OkayCancelListener implements ActionListener {
        private final WriteToFile writingToFile = new WriteToFile();
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == okay) {
            //if okay is clicked
                String getBillSelected = billsBox.getSelectedItem().toString();//grabs bills[] 
                
                if (!getBillSelected.equals("Other")) {
                //if anything but other was selected do this
                    String getRadioValue = null;
                    
                    boolean isOtherPaymentButtonSelected = paymentsButton[8].isSelected();
                    //if other jRadioButton was selected.
                    if (isOtherPaymentButtonSelected) {
                        String money = "$";//making the save file read $xx.xx
                        getRadioValue = money.concat(manualInput.getText());
                    } else {
                        for (JRadioButton paymentsButton1 : paymentsButton) {
                            if (paymentsButton1.isSelected()) {
                                getRadioValue = paymentsButton1.getText();
                            }
                        }
                    }
                    String newEvent =getBillSelected+" "+getRadioValue;
                    pojo.setEvent(newEvent);
                    writingToFile.writeEventToFile(pojo);
                } 
                else {//else means that 'other' was selected.
                    String titleOfDescription = descriptionTitle.getText();
                    pojo.setEvent(titleOfDescription);
                    writingToFile.writeEventToFile(pojo);
                }
            }
            else if (e.getSource() == cancel) {//if cancel is pressed do nothing

            }

            //making the tabs 'clickable' again
            theAppTabs.setEnabledAt(0, true);
            theAppTabs.setEnabledAt(1, true);
            //remove creating an event tab
            theAppTabs.remove(2);
            theAppTabs.setSelectedIndex(0);//when closed put focus on event tab
        }

    }

   
    /**
     * handles when clicking into text box the text disapears
     * and will repear if you click off of the text box without writing anything into it 
     */
    private class textTitleAction implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            if (descriptionTitle.getText().equals("Enter your event title")) {
                descriptionTitle.setText("");
            } else
                ;
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (descriptionTitle.getText().equals("")) {
                descriptionTitle.setText("Enter your event title");
            }
        }

    }//end of text title action

    private class ComboBoxListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            descriptionPanel.removeAll();
            switch (billsBox.getSelectedIndex()) {
                //case 0,1,2,3,4 are Comcast,Electric,Gas,Phone,Insurance
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                    displayRadioButton();
                    break;
                case 5://case 5 brings up a text box to plan a custom event such as a birthday 
                    textTitleAction textTitle = new textTitleAction();
                    Box vert = Box.createVerticalBox();
                    descriptionTitle = new JTextField(30);
                    descriptionTitle.setText("Enter your event title");
                    descriptionTitle.addFocusListener(textTitle);
                    vert.add(descriptionTitle);
                    descriptionPanel.add(vert);
                    descriptionPanel.updateUI();
                    break;
            }
        }

    }//end comboBoxListener

}//endEventForm