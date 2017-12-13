Attribute VB_Name = "Module"
Option Explicit

' ���ݑI�����Ă���Z��
Dim currentSelect As Range

' ***** Config *****

Const eviNoSeparate As Long = 1
Const abbSeparate As Long = 20

' ******************


' xml�ꎞ�ǂݍ��ݗp
Dim xml As MSXML2.DOMDocument60

Public Sub �G�r�f���X�\��t��v3()
    
    ' �I���ʒu���N�_�ɃX�^�[�g
    Set currentSelect = cells(Selection.row, Selection.Column)
    
    ' �G�r�f���X�t�H���_�[�擾
    Dim path As String
    path = GetFolderPath()
    
    ' �C����t�H���_�[
    Dim afterPath As String
    afterPath = path & "\after"
    
    ' �C���O�t�H���_�[
    Dim beforePath As String
    beforePath = path & "\before"
    
    ' �C����t�H���_�[���̃G�r�f���X�t�H���_�[�ꗗ���擾
    ' �C���O�t�H���_�[���̃G�r�f���X�t�H���_�[�ꗗ�͏C����Ɠ����͂�������m�F���Ȃ�
    If Dir(afterPath, vbDirectory) <> "" Then
        Dim afterFolderList As Collection
        Set afterFolderList = New Collection
        Set xml = New MSXML2.DOMDocument60
        xml.Load (afterPath & "\" & "FolderList.xml")
        Dim afterFolders As IXMLDOMNode
        Set afterFolders = xml.SelectSingleNode("//folderList")
        Dim afterFolder As IXMLDOMNode
        For Each afterFolder In afterFolders.ChildNodes
            afterFolderList.Add afterFolder.text
        Next afterFolder
    Else
        MsgBox "�C����t�H���_�[������܂���B", Buttons:=vbCritical
        Exit Sub
    End If
    
    ' �t�H���_�[�ŌJ��Ԃ�
    Dim folder As Variant
    For Each folder In afterFolderList
        
        Dim startSelect As Range
        Set startSelect = Range(currentSelect.Address)
        
        ' �C����t�H���_�[����info�t�@�C���ꗗ�擾
        Dim afterInfoList As Collection
        Set afterInfoList = GetInfoFiles(afterPath & "\" & folder)
        
        ' info�t�@�C��������G�r�f���X�����擾
        Dim infoCount As Long
        infoCount = afterInfoList.Count
        
        ' �G�r�f���X�������J��Ԃ�
        Dim index As Long
        For index = 1 To infoCount
            
            ' order����info�擾
            Dim infoXml As MSXML2.DOMDocument60
            Set infoXml = GetInfoByOrder(afterInfoList, index - 1)
            
            ' �G�r�f���X���擾
            Dim eviNo As String
            eviNo = infoXml.SelectSingleNode("//evidenceInformation//evidence_name").text
            ' �G�r�f���X����������
            WriteEvidenceNo eviNo
            
            ' �G�r�f���X���Z���擾
            Dim eviNoCell As Range
            Set eviNoCell = Range(infoXml.SelectSingleNode("//evidenceInformation//evi_no_address").text)
            
            ' �e�X�g�X�E�B�[�g�V�[�g�Ɉړ�
            ActiveWorkbook.Worksheets(eviNoCell.Parent.Name).Activate
            
            ' �G�r�f���X���Z���ɒl������
            eviNoCell.Value = eviNoCell.Value & eviNo & vbLf
            
            ' �G�r�f���X�V�[�g�ɖ߂�
            ActiveWorkbook.Worksheets(currentSelect.Parent.Name).Activate
                        
            ' �R���e���c�m�[�h�擾
            Dim contents As String
            contents = infoXml.SelectSingleNode("//evidenceInformation//contents").text
            ' �R���e���c��������
            WriteContents contents
            
            ' �G�r�f���X�摜�֘A�̒l�擾
            Dim imageName As String
            imageName = afterPath & "\" & folder & "\" & infoXml.SelectSingleNode("//evidenceInformation//image_name").text
            Dim imageWidth As Long
            imageWidth = infoXml.SelectSingleNode("//evidenceInformation//image_width").text
            Dim imageHeight As Long
            imageHeight = infoXml.SelectSingleNode("//evidenceInformation//image_height").text
            ' �G�r�f���X�摜�\��t��
            WriteEvidenceImage imageName, imageWidth, imageHeight
            
            Set currentSelect = currentSelect.Offset(imageHeight / currentSelect.Height, -eviNoSeparate)
            
        Next index
        
        
        ' �C���O�t�H���_�[����info�t�@�C���ꗗ�擾
        Dim beforeInfoList As Collection
        If Dir(beforePath, vbDirectory) <> "" Then
            Set beforeInfoList = GetInfoFiles(beforePath & "\" & folder)
            Dim buf_currentSelect As Range
            Set buf_currentSelect = Range(currentSelect.Address)
            Set currentSelect = startSelect.Offset(0, abbSeparate)
        Else
            GoTo CONTINUE
        End If
        
        ' info�t�@�C��������G�r�f���X�����擾
        infoCount = beforeInfoList.Count
        
        ' �G�r�f���X�������J��Ԃ�
        For index = 1 To infoCount
            
            ' order����info�擾
            Set infoXml = GetInfoByOrder(beforeInfoList, index - 1)
            
            ' �R���e���c�m�[�h�擾
            contents = infoXml.SelectSingleNode("//evidenceInformation//contents").text
            ' �R���e���c��������
            WriteContents contents
            
            ' �G�r�f���X�摜�֘A�̒l�擾
            imageName = beforePath & "\" & folder & "\" & infoXml.SelectSingleNode("//evidenceInformation//image_name").text
            imageWidth = infoXml.SelectSingleNode("//evidenceInformation//image_width").text
            imageHeight = infoXml.SelectSingleNode("//evidenceInformation//image_height").text
            ' �G�r�f���X�摜�\��t��
            WriteEvidenceImage imageName, imageWidth, imageHeight
            
            Set currentSelect = currentSelect.Offset(imageHeight / currentSelect.Height, 0)
            
        Next index
        
        Set currentSelect = Range(buf_currentSelect.Address)
        
CONTINUE:
    Next folder
    
End Sub

' �t�H���_�[�p�X�擾�p�̃_�C�A���O�\��
Private Function GetFolderPath() As String
    Dim xlAPP As Application
    Set xlAPP = Application
    GetFolderPath = xlAPP.InputBox("�G�r�f���X�̊i�[�t�H���_������͂��ĉ������B", _
                                 "�t�H���_�I��", "C:\")
    Set xlAPP = Nothing
End Function

' info�t�@�C���̈ꗗ���擾
Private Function GetInfoFiles(folder As String) As Collection
    
    ' �󕶎��Ȃ��̃R���N�V���������^�[��
    If "" = folder Then
        Set GetInfoFiles = New Collection
        Exit Function
    End If
        
    Dim file As String
    file = Dir(folder & "\*.info", vbNormal)
    
    ' info�t�@�C���̈ꗗ���擾
    Dim infoList As New Collection
    Do While file <> ""
        
        ' info�t�@�C�����R���N�V�����ɒǉ�
        infoList.Add folder & "\" & file
        
        ' ����info�t�@�C����
        file = Dir()
    Loop
    Set GetInfoFiles = infoList
    
End Function

' info���X�g�̒�����w�肵��order��xml���擾
Private Function GetInfoByOrder(list As Collection, order As Long) As MSXML2.DOMDocument60
    
    ' info���X�g�̃T�C�Y���J��Ԃ�
    Dim index As Long
    For index = 1 To list.Count
        ' xml�I�u�W�F�N�g�쐬
        Set xml = New MSXML2.DOMDocument60
        
        ' info�t�@�C���ꎞ�i�[
        Dim infoFile As String
        infoFile = list.Item(index)
        
        ' info�t�@�C���ǂݍ���
        xml.Load (infoFile)
        
        ' order�̒l���m�F
        Dim orderNode As IXMLDOMNode
        Set orderNode = xml.SelectSingleNode("//evidenceInformation//order")
        If orderNode.text = order Then
            Set GetInfoByOrder = xml
            Exit Function
        End If
        
        Set xml = Nothing
    Next index
    
    ' ������Ȃ�����Nothing�����^�[��
    Set GetInfoByOrder = Nothing
    
End Function

' �G�r�f���X�������o��
Private Function WriteEvidenceNo(text As String)
    currentSelect.Value = text
    Set currentSelect = currentSelect.Offset(0, eviNoSeparate)
End Function

' �R���e���c�����o��
Private Function WriteContents(contents As String)
    Dim lines As Variant
    Dim line As Variant
    
    ' �R���e���c�����s�R�[�h�ŕ���
    lines = Split(contents, "\n")
    For Each line In lines
        
        ' �����󕶎�����Ȃ��Ȃ�
        If line <> "" Then
            
            ' �Z���ɒl������
            currentSelect.Value = line
            
            ' ���̃Z����
            Set currentSelect = currentSelect.Offset(1, 0)
        End If
    Next line
End Function

' �G�r�f���X�摜�\��t�� �Z���̈ړ�����ԋp
Private Function WriteEvidenceImage(imagePaht As String, imageWidth As Long, imageHeight As Long)
    currentSelect.Activate
    Dim imageShape As Shape
    Set imageShape = ActiveSheet.Shapes.AddPicture( _
        Filename:=imagePaht, _
        LinkToFile:=False, _
        SaveWithDocument:=True, _
        Left:=Selection.Left, _
        Top:=Selection.Top, _
        Width:=imageWidth, _
        Height:=imageHeight)
    Set imageShape = Nothing
End Function


