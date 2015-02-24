package json.data;

/**
 * Created by KrabiySok on 2/23/2015.
 */
public class DayInfo {
    double pressure;
    byte humidity;
    double speed;
    double degree;
    byte clouds;
    String main;
    String description;
    long dt;
    Temperature temperature = new Temperature();

    class Temperature {
        double day;
        double min;
        double max;
        double night;
        double evening;
        double morning;

        public double getDay() {
            return day;
        }

        public void setDay(double day) {
            this.day = day;
        }

        public double getMin() {
            return min;
        }

        public void setMin(double min) {
            this.min = min;
        }

        public double getMax() {
            return max;
        }

        public void setMax(double max) {
            this.max = max;
        }

        public double getNight() {
            return night;
        }

        public void setNight(double night) {
            this.night = night;
        }

        public double getEvening() {
            return evening;
        }

        public void setEvening(double evening) {
            this.evening = evening;
        }

        public double getMorning() {
            return morning;
        }

        public void setMorning(double morning) {
            this.morning = morning;
        }
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String icon;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public byte getHumidity() {
        return humidity;
    }

    public void setHumidity(byte humidity) {
        this.humidity = humidity;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDegree() {
        return degree;
    }

    public void setDegree(double degree) {
        this.degree = degree;
    }

    public byte getClouds() {
        return clouds;
    }

    public void setClouds(byte clouds) {
        this.clouds = clouds;
    }
}
