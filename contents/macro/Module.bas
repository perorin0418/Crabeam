Attribute VB_Name = "Module"
Option Explicit

' ���ݑI�����Ă���Z��
Public currentSelect As Range

' xml�ꎞ�ǂݍ��ݗp
Public xml As MSXML2.DOMDocument

Public Sub �G�r�f���X�\��t��v3()
    
    ' �I���ʒu���N�_�ɃX�^�[�g
    Set currentSelect = cells(Selection.row, Selection.Column)
    
    ' �C����t�H���_�[�擾
    Dim afterPath As String
    afterPath = GetFolderPath("�C����")
    If "" = afterPath Then
        MsgBox "�����͂ł�OK���ƌ������ȁB" & vbLf & "����͉R���B�C����͓���Ȃ��Ƃ��߂��B", vbExclamation, _
        "�t�H���_���̃t�@�C�����ꗗ�擾"
    End If
    
    ' �C���O�t�H���_�[�擾
    Dim beforePath As String
    beforePath = GetFolderPath("�C���O")
    
    ' �C����t�H���_�[���̃G�r�f���X�t�H���_�[�ꗗ���擾
    Dim afterFolderList As Collection
    Set afterFolderList = New Collection
    Set xml = New MSXML2.DOMDocument
    xml.Load (afterPath & "\" & "FolderList.xml")
    Dim afterFolders As IXMLDOMNode
    Set afterFolders = xml.SelectSingleNode("//folderList")
    Dim afterFolder As IXMLDOMNode
    For Each afterFolder In afterFolders.ChildNodes
        afterFolderList.Add afterFolder.text
    Nexe afterFolder
    
    
    
    
    
    
    
    
    
    
    
    ' �C����t�H���_�[����info�t�@�C���ꗗ�擾
    Dim afterInfoList As Collection
    Set afterInfoList = GetInfoFiles(afterPath)
    
    ' �C���O�t�H���_�[����info�t�@�C���ꗗ�擾
    Dim beforeInfoList As Collection
    Set beforeInfoList = GetInfoFiles(beforePath)
    
    ' info�t�@�C��������G�r�f���X�����擾
    Dim infoCount As Long
    infoCount = afterInfoList.Count
    If infoCount < beforeInfoList.Count Then
        infoCount = beforeInfoList.Count
    End If
    
    ' �G�r�f���X�������J��Ԃ�
    Dim index As Long
    For index = 0 To infoCount
        ' order����info�擾
        Dim infoXml As MSXML2.DOMDocument
        Set infoXml = GetInfoByOrder(afterInfoList, index)
        
        ' node�i�[�p
        Dim node As IXMLDOMNode
        
        ' �R���e���c�m�[�h�擾
        Set node = infoXml.SelectSingleNode("//evidenceInformation//contents")
        ' �R���e���c��������
        WriteContents (node.text)
        
        
        
    Next index
    
    
    
    
    
End Sub

' �t�H���_�[�p�X�擾�p�̃_�C�A���O�\��
Private Function GetFolderPath(boa As String) As String
    Dim xlAPP As Application
    Set xlAPP = Application
    GetFolderPath = xlAPP.InputBox(boa & "�̃G�r�f���X�̃t�H���_������͂��ĉ������B" & vbLf & "�����ꍇ�͖����͂łn�j�ł��B", _
                                 "�t�H���_���̃t�@�C�����ꗗ�擾", "C:\")
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
    file = Dir(folder & "*.info", vbNormal)
    
    ' info�t�@�C���̈ꗗ���擾
    Dim infoList As New Collection
    Do While strFileName <> ""
        
        ' info�t�@�C�����R���N�V�����ɒǉ�
        infoList.Add file
        
        ' ����info�t�@�C����
        file = Dir()
    Loop
    Set GetInfoFiles = infoList
    
End Function

' info���X�g�̒�����w�肵��order��xml���擾
Private Function GetIndoByOrder(list As Collection, order As Long) As MSXML2.DOMDocument
    
    info���X�g�̃T�C�Y���J��Ԃ�
    Dim index As Long
    For index = 0 To list.Count
        ' xml�I�u�W�F�N�g�쐬
        Set xml = New MSXML2.DOMDocument
        
        ' info�t�@�C���ꎞ�i�[
        Dim infoFile As String
        infoFile = list.Item(index)
        
        ' info�t�@�C���ǂݍ���
        xml.Load (infoFile)
        
        ' order�̒l���m�F
        Dim orderNode As IXMLDOMNode
        Set orderNode = xml.SelectSingleNode("//evidenceInformation//order")
        If orderNode.text = order Then
            Set GetIndoByOrder = xml
            Exit Function
        End If
        
        Set xml = Nothing
    Next index
    
    ' ������Ȃ�����Nothing�����^�[��
    Set GetIndoByOrder = Nothing
    
End Function

' �G�r�f���X�������o��
Private Function WriteEvidenceNo(text As String)
    currentSelect.Value = text
    Set currentSelect = currentSelect.Offset(0, 1)
End Function

' �R���e���c�����o��
Private Function WriteContents(contents As String)
    Dim lines As Variant
    Dim line As String
    
    ' �R���e���c�����s�R�[�h�ŕ���
    Set lines = Split(contents, Chr(10))
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


