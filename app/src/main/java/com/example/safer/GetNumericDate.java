package com.example.safer;

public class GetNumericDate {
    private String strMonth;
    private String strDay;
    private String stryear;
    private String strTime;
    private String strHour;
    private String strMinute;

    public GetNumericDate(String strDate, String strTime){
        String[] dateArr = strDate.split(" ");
        this.strMonth = dateArr[0];
        this.strDay = dateArr[1];
        if (this.strDay.length() == 2) this.strDay = "0" + this.strDay;
        this.stryear = dateArr[2];

        String[] timeArr = strTime.split(":");
        this.strHour = timeArr[0];
        if (this.strHour.length() == 1) this.strHour = "0" + this.strHour;
        this.strMinute = timeArr[1];
        if (this.strMinute.length() == 1) this.strMinute = "0" + this.strMinute;

        this.strTime = this.strHour + this.strMinute;

    }

    public String transform(){
        String numMonth = "";
        String res = null;

         switch (this.strMonth){
             case "Jan": {numMonth = "01"; break;}
             case "Feb": {numMonth = "02"; break;}
             case "Mar": {numMonth = "03"; break;}
             case "Apr":{numMonth = "04"; break;}
             case "May": {numMonth = "05"; break;}
             case "June": {numMonth = "06"; break;}
             case "July": {numMonth = "07"; break;}
             case "Aug": {numMonth = "08"; break;}
             case "Sept": {numMonth = "09"; break;}
             case "Oct": {numMonth = "10"; break;}
             case "Nov": {numMonth = "11"; break;}
             case "Dec": {numMonth = "12"; break;}
             default: res = "error";
        }

        if (numMonth != ""){
            res = stryear + numMonth + strDay + strTime;
        }
        return res;
    }
}
