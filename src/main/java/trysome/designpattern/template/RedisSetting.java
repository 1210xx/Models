package trysome.designpattern.template;

public class RedisSetting extends AbstractSetting{
    @Override
    protected String lookupCache(String key) {
        return null;
    }

    @Override
    protected void putIntoCache(String key, String value) {

    }
//    private RedisClient client = RedisClient.create("redis://localhost:6379");
//
//    protected String lookupCache(String key) {
//        try (StatefulRedisConnection<String, String> connection = client.connect()) {
//            RedisCommands<String, String> commands = connection.sync();
//            return commands.get(key);
//        }
//    }
//
//    protected void putIntoCache(String key, String value) {
//        try (StatefulRedisConnection<String, String> connection = client.connect()) {
//            RedisCommands<String, String> commands = connection.sync();
//            commands.set(key, value);
//        }
//    }
}
