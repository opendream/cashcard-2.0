package th.co.opendream.cashcard

class RunNo {

    String key
    String description
    Integer currentNo
    Integer padSize
    String prefix
    String suffix
    // template (ถ้าไม่มี tpl ให้ใช้ค่าจาก prefix, suffix แทน)
    String template

    static constraints = {
        key(unique : true, maxSize: 40)
        prefix(nullable : true, maxSize: 12)
        suffix(nullable : true, maxSize: 12)
        template(nullable : true, maxSize: 50)
    }

}
