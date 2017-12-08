import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Loan_process {

    public static class LoanInfo {
        public int months;
        public Date loan_date;
        public double percent;
        private double amount, curr_amount;

        public void setAmount(double amount) {
            this.amount = amount;
            curr_amount = amount;
        }

        public double getAmount() {
            return amount;
        }

        public void setCurr_amount(double curr_amount) {
            this.curr_amount = curr_amount;
        }

        public double getCurr_amount() {
            return curr_amount;
        }
    }

    public static LoanInfo get_info() throws ParseException {
        LoanInfo info = new LoanInfo();

        Scanner in = new Scanner(System.in);

        System.out.print("Enter day of loan: ");
        DateFormat df = DateFormat.getDateInstance();
        try {
            info.loan_date = df.parse(in.next());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.print("Enter term in months: ");
        info.months = in.nextInt();

        System.out.print("Enter amount of loan: ");
        info.amount = in.nextDouble();

        System.out.print("Enter percent: ");
        info.percent = in.nextDouble();

        return info;
    }

    public static Date addMonth(Date date, int months)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    public static Schedule get_next_Schedule(int month, LoanInfo loanInfo) {
        Schedule schedule = new Schedule();

        schedule.setDate(addMonth(loanInfo.loan_date, month));

        double loan_payment = get_loan_payment(loanInfo.amount, loanInfo.months);
        double sum = loanInfo.amount - loan_payment*(month);

        schedule.setAmount_loan(loan_payment);
        schedule.setAmount_prc(get_percent_payment(sum, loanInfo.percent));

        return schedule;
    }

    private static double get_loan_payment(double amount, int months) {
        return amount/months;
    }

    private static double get_percent_payment(double amount, double percent) {
        double monthly_percent = get_monthly_percent(percent);
        return amount * monthly_percent;
    }

    private static double get_monthly_percent(double percent) {
        return percent / 100 / 12;
    }

    public static void main(String[] args) throws ParseException {
        LoanInfo loanInfo = get_info();

        ArrayList<Schedule> scheduleList = new ArrayList<>();

        for(int month = 0; month < loanInfo.months; ++month) {
            Schedule schedule =get_next_Schedule(month, loanInfo);
            scheduleList.add(schedule);
            System.out.printf("%10.2f %10.2f %10.2f\n", schedule.getAmount_loan(), schedule.getAmount_prc(),
                    schedule.getAmount_loan() + schedule.getAmount_prc());
        }
    }
}
