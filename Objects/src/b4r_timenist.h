
#ifndef b4r_timenist_h
#define b4r_timenist_h
class b4r_main;

class b4r_timenist {
public:

static void _astream_error();
static void _astream_newdata(B4R::Array* _buffer);
static void _connect(Byte _u);
static B4R::Array* _getdate();
static UInt _gethours();
static UInt _getminutes();
static UInt _getseconds();
static ULong _getupdatedtotalseconds();
static void _process_globals();
static B4R::Timer* _tmr;
static B4R::WiFiSocket* _socket;
static ULong _lastmillis;
static B4R::AsyncStreams* _astream;
static B4R::Array* _date;
static B4R::ByteConverter* _bc;
static ULong _seconds;
static bool _firsttime;
static b4r_main* _main;
static void _start();
static void _tmr_tick();
};

#endif