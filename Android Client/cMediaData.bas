B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Class
Version=4.3
@EndOfDesignText@
'Class module
Sub Class_Globals
'    Private Const METADATA_KEY_ALBUM            As Int     =  1       ' The metadata key to retrieve the information about the album title of the data source.
'    Private Const METADATA_KEY_ALBUMARTIST      As Int     = 13       ' The metadata key to retrieve the information about the performers or artist associated with the data source.
'    Private Const METADATA_KEY_ARTIST           As Int     =  2       ' The metadata key to retrieve the information about the artist of the data source.
'    Private Const METADATA_KEY_AUTHOR           As Int     =  3       ' The metadata key to retrieve the information about the author of the data source.
'    Private Const METADATA_KEY_BITRATE          As Int     = 20       ' This key retrieves the average bitrate (in bits/sec), if available.
'    Private Const METADATA_KEY_CD_TRACK_NUMBER  As Int     =  0       ' The metadata key to retrieve the numeric string describing the order of the audio data source on its original recording.
'    Private Const METADATA_KEY_COMPILATION      As Int     = 15       ' The metadata key to retrieve the music album compilation status.
'    Private Const METADATA_KEY_COMPOSER         As Int     =  4       ' The metadata key to retrieve the information about the composer of the data source.
'    Private Const METADATA_KEY_DATE             As Int     =  5       ' The metadata key to retrieve the date when the data source was created or modified.
'    Private Const METADATA_KEY_DISC_NUMBER      As Int     = 14       ' The metadata key to retrieve the numberic string that describes which part of a set the audio data source comes from.
    Private Const METADATA_KEY_DURATION         As Int     =  9       ' The metadata key to retrieve the playback duration of the data source.
'    Private Const METADATA_KEY_GENRE            As Int     =  6       ' The metadata key to retrieve the content type or genre of the data source.
'    Private Const METADATA_KEY_HAS_AUDIO        As Int     = 16       ' If this key exists the media contains audio content.
'    Private Const METADATA_KEY_HAS_VIDEO        As Int     = 17       ' If this key exists the media contains video content.
'    Private Const METADATA_KEY_LOCATION         As Int     = 23       ' This key retrieves the location information, if available.
'    Private Const METADATA_KEY_MIMETYPE         As Int     = 12       ' The metadata key to retrieve the mime type of the data source.
'    Private Const METADATA_KEY_NUM_TRACKS       As Int     = 10       ' The metadata key to retrieve the number of tracks, such as audio, video, text, in the data source, such as a mp4 or 3gpp file.
'    Private Const METADATA_KEY_TITLE            As Int     =  7       ' The metadata key to retrieve the data source title.
'    Private Const METADATA_KEY_VIDEO_HEIGHT     As Int     = 19       ' If the media contains video, this key retrieves its height.
'    Private Const METADATA_KEY_VIDEO_ROTATION   As Int     = 24       ' This key retrieves the video rotation angle in degrees, if available.
'    Private Const METADATA_KEY_VIDEO_WIDTH      As Int     = 18       ' If the media contains video, this key retrieves its width.
'    Private Const METADATA_KEY_WRITER           As Int     = 11       ' The metadata key to retrieve the information of the writer (such as lyricist) of the data source.
'    Private Const METADATA_KEY_YEAR             As Int     =  8       ' The metadata key to retrieve the year when the data source was created or modified.

    Private Const SetNotCalledError             As String  = "ProcessMediaFile has not been called"

    Private       FD_Path                       As String
    Private       FD_File                       As String

    Private       FD_Combined                   As String

    Private       ReflectorIsValid              As Boolean
    Private       ReflectedFile                 As Reflector
End Sub

'Initializes the object. You can add parameters to this method if needed.
Public Sub Initialize

    FD_Path          = ""
    FD_File          = ""
    FD_Combined      = ""

    ReflectorIsValid = False
End Sub


Public Sub ProcessMediaFile(FilePath As String, FileName As String) As Boolean

    If File.Exists(FilePath, FileName) = False Then Return False

    FD_Path     = FilePath
    FD_File     = FileName
    FD_Combined = File.Combine(FD_Path, FD_File)

    Try
        Dim Obj As Object = ReflectedFile.CreateObject("android.media.MediaMetadataRetriever")

        ReflectedFile.Target = Obj

        Try
            ReflectedFile.RunMethod2("setDataSource", FD_Combined, "java.lang.String")
            ReflectorIsValid = True
			
			Return True
        Catch
            Msgbox("File cannot be read. Is it a valid Media File?", "Error")
            Return False
        End Try
	Catch
        Msgbox("Unknown Error on Meddia File?", "Error")
        Return False	
	End Try
End Sub

'Public  Sub GetAlbumName           As String  :  Return GetMetaData("GetAlbumName"    , METADATA_KEY_ALBUM          )  :  End Sub
'Public  Sub GetAlbumArtist         As String  :  Return GetMetaData("GetAlbumArtist"  , METADATA_KEY_ALBUMARTIST    )  :  End Sub
'Public  Sub GetArtist              As String  :  Return GetMetaData("GetArtist"       , METADATA_KEY_ARTIST         )  :  End Sub
'Public  Sub GetAuthor              As String  :  Return GetMetaData("GetAuthor"       , METADATA_KEY_AUTHOR         )  :  End Sub
'Public  Sub GetBitRate             As String  :  Return GetMetaData("GetBitRate"      , METADATA_KEY_BITRATE        )  :  End Sub
'Public  Sub GetCDTrackNumber       As String  :  Return GetMetaData("GetCDTrackNumber", METADATA_KEY_CD_TRACK_NUMBER)  :  End Sub
'Public  Sub GetCompliation         As String  :  Return GetMetaData("GetCompliation"  , METADATA_KEY_COMPILATION    )  :  End Sub
'Public  Sub GetComposer            As String  :  Return GetMetaData("GetComposer"     , METADATA_KEY_COMPOSER       )  :  End Sub
'Public  Sub GetDate                As String  :  Return GetMetaData("GetDate"         , METADATA_KEY_DATE           )  :  End Sub
'Public  Sub GetDiscNumber          As String  :  Return GetMetaData("GetDiscNumber"   , METADATA_KEY_DISC_NUMBER    )  :  End Sub
Public  Sub GetDuration As String
	Return GetMetaData("GetDuration", METADATA_KEY_DURATION)
End Sub
'Public  Sub GetGenre               As String  :  Return GetMetaData("GetGenre"        , METADATA_KEY_GENRE          )  :  End Sub
'Public  Sub GetHasAudio            As String  :  Return GetMetaData("GetHasAudio"     , METADATA_KEY_HAS_AUDIO      )  :  End Sub
'Public  Sub GetHasVideo            As String  :  Return GetMetaData("GetHasVideo"     , METADATA_KEY_HAS_VIDEO      )  :  End Sub
'Public  Sub GetLocation            As String  :  Return GetMetaData("GetLocation"     , METADATA_KEY_LOCATION       )  :  End Sub
'Public  Sub GetMimeType            As String  :  Return GetMetaData("GetMimeType"     , METADATA_KEY_MIMETYPE       )  :  End Sub
'Public  Sub GetNumberTracks        As String  :  Return GetMetaData("GetNumberTracks" , METADATA_KEY_NUM_TRACKS     )  :  End Sub
'Public  Sub GetTitle               As String  :  Return GetMetaData("GetTitle"        , METADATA_KEY_TITLE          )  :  End Sub
'Public  Sub GetVideoHeight         As String  :  Return GetMetaData("GetVideoHeight"  , METADATA_KEY_VIDEO_HEIGHT   )  :  End Sub
'Public  Sub GetVideoRotation       As String  :  Return GetMetaData("GetVideoRotation", METADATA_KEY_VIDEO_ROTATION )  :  End Sub
'Public  Sub GetVideoWidth          As String  :  Return GetMetaData("GetVideoWidth"   , METADATA_KEY_VIDEO_WIDTH    )  :  End Sub
'Public  Sub GetWriter              As String  :  Return GetMetaData("GetWriter"       , METADATA_KEY_WRITER         )  :  End Sub
'Public  Sub GetYear                As String  :  Return GetMetaData("GetYear"         , METADATA_KEY_YEAR           )  :  End Sub

Private Sub GetMetaData(FunctionCalled As String, FieldNumber As Int) As String

        If ReflectorIsValid = False Then Return SetNotCalledError

        Try
           Dim ReturnData As String = ReflectedFile.RunMethod2("extractMetadata", FieldNumber, "java.lang.int")
		   
		   '----------------------------------------------------------------------------------------
		   '  If Data is NOT present the word null is returned - we do not want to see this
		   '----------------------------------------------------------------------------------------
		   If ReturnData = "null"  Then Return ""
		   
		   Return ReturnData
        Catch
           Return "Error on "&FunctionCalled
        End Try
End Sub

Public  Sub GetEmbeddedPicture As BitmapDrawable

		If ReflectorIsValid = False Then Return Null

        Try
           Dim ReturnData() As Byte = ReflectedFile.RunMethod("getEmbeddedPicture")
		   
		   If ReturnData <> Null And ReturnData.Length <> 0 Then  
              Dim InStream          As InputStream
              Dim CoverArt          As Bitmap
			  
			  Dim CoverArtDrawable  As BitmapDrawable
			  
              InStream.InitializeFromBytesArray(ReturnData, 0, ReturnData.Length)
              CoverArt.Initialize2(InStream)
			  CoverArtDrawable.Initialize(CoverArt)
			  
              Return CoverArtDrawable
           End If
		   
		   Return Null
        Catch
           Return Null
        End Try
End Sub
