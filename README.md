### CRUD效率对比
Demo用100000（十万）条数据进行批量的插入、查询、删除操作，得出如下统计数据：

|   操作(单位：ms)  |    greenDao          | Room           | liteOrm
| -------------   | -------------------- | -------------- | --------------
|   insertAll     | 1651.2               | 1603.8         | 1876.2
|   selectAll     | 874                  | 1287.4         | 1475.5
|   deledtAll     | 669.8                | 49.5           | 53.8

以上统计数据为5次操作的平均值

### 数据库版本升级对比
#### greenDao
greenDao的数据库版本可以在app的build.gradle中指定，如：
```
android {
 
    greendao {
        //指定数据库schema版本号，迁移等操作会用到
        schemaVersion 1
        //DaoSession、DaoMaster以及所有实体类的dao生成的目录
        //daoPackage 包名
        daoPackage 'com.example.liaoshouwang.dbdemo.greendaodb'
        //工程路径
        targetGenDir 'src/main/java'
    }
}
```
处理升级需继承 DaoMaster.OpenHelper，重写onUpgrade方法，在onUpgrade方法中实现所需的升级操作。
greenDao中有默认的OpenHelper实现类，其内部实现如下：
```
//默认每次升级会把数据库中的数据清除
/** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }
```
#### Room
Room的数据库升级也需要手动添加代码处理，否则也是会默认清除数据库里的数据。
```
Room.databaseBuilder(getApplicationContext(), MyDb.class, "database-name")
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3).build();

//1，2是对应的旧、新的数据库版本
static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
        database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, "
                + "`name` TEXT, PRIMARY KEY(`id`))");
    }
};

static final Migration MIGRATION_2_3 = new Migration(2, 3) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
        database.execSQL("ALTER TABLE Book "
                + " ADD COLUMN pub_year INTEGER");
    }
};
```
#### LiteOrm
Lite的数据库升级对应外部是无感知的，当数据库中的表新增或者减少字段时，Lite会自动处理升级，且保留旧的数据。

### 自定义SQL语句的支持程度
#### greenDao
greenDao有两种办法嵌入自定义的SQL语句，分别是通过queryRaw和queryRawCreate
```
/** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<T> queryRaw(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(statements.getSelectAll() + where, selectionArg);
        return loadAllAndCloseCursor(cursor);
    }

    /**
     * Creates a repeatable {@link Query} object based on the given raw SQL where you can pass any WHERE clause and
     * arguments.
     */
    public Query<T> queryRawCreate(String where, Object... selectionArg) {
        List<Object> argList = Arrays.asList(selectionArg);
        return queryRawCreateListArgs(where, argList);
    }
```
可以看到的是，这两个方法对于自定义的SQL语句，其实是WHERE后面的那部分，让我们来看看statements.getSelectAll()返回的是什么。
```
SELECT T."_id",T."NAME",T."AGE" FROM "GREEN_USER" T 
```
GREEN_USER是Demo中的表名，也就是说greenDao会在我们的SQL语句前面**自动**拼接上上面的这段SQL，这段SQL其实是相对于SELECT * FROM tablename

#### Room
Room对于自定义SQL可以说是支持得非常完美，因为Room的Dao中所有的CRUD方法都是通过我们自己写SQL去实现的，比如Demo中的RoomUserDao:
```
@Dao
interface RoomUserDao {

    @Query("SELECT * FROM ROOM_USER")
    fun getRoomUsers(): List<RoomUser>

    @Insert
    fun insertAll(users:List<RoomUser>)

    @Query("DELETE FROM ROOM_USER")
    fun deleteAll()


}
```
更复杂一点的SQL，也是没问题的：
```
@Dao
interface MyAppReportDataDao {

    //本周每天的点击次数，intervalDay栗子：-7 day 指定的日期往前回滚7天，
    //datetime('now','start of day',:intervalDay)，当前日期往前回滚intervalDay天
    @Query("SELECT count(*) as times ,time_date as date " +
            "FROM ( SELECT * FROM MY_APP_REPORT_DATA WHERE time_date>=datetime('now','start of day',:intervalDay) " +
            "AND time_date<=datetime('now','localtime')) WHERE time_date>=datetime(time_date,'start of day','+0 day') " +
            "AND time_date<datetime(time_date,'start of day','+1 day') group by date(time_date)")
    fun getLineDataByWeek(intervalDay:String):LiveData<List<ReportLineData>>

```
#### LiteOrm
LiteOrm的查询操作都是通过whereBuilder来实现的，例如：
```
liteOrm.query(new QueryBuilder<Book>(Book.class)
        .columns(new String[]{"id", "author", Book.COL_INDEX})
        .distinct(true)
        .whereGreaterThan("id", 0)
        .whereAppendAnd()
        .whereLessThan("id", 10000)
        .limit(6, 9)
        .appendOrderAscBy(Book.COL_INDEX));
```
其对于自定义的SQL支持可谓是最低的，毕竟该项目已经处于停更的状态了（小闹钟用的是这个框架）

### 总结
从性能上来说，greenDao和Room都要优于LiteOrm，而greenDao和Room两者对比，Room的批量删除是完胜的；
从自定义SQL的支持程度来说，Room完胜；
数据库的升级操作，greenDao和Room都需要开发者手动处理；
值得注意的是，Room的所有CRUD操作都必须要在线程中完成，有且仅有一种情况允许不在线程中执行，那就是CRUD操作的是LiveData类型的数据，
Room是支持LiveData的；
另外，LiteOrm已处于停更状态，选择框架的时候要谨慎考虑。








