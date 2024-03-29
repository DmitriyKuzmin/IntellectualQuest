package com.kdp.quest.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kdp.quest.MainActivity;
import com.kdp.quest.R;
import com.kdp.quest.adapter.SpecialtyAdapter;
import com.kdp.quest.model.Specialty;

import java.util.ArrayList;
import java.util.List;

public class FinishFragment extends Fragment {

    private static final String TAG = FinishFragment.class.getSimpleName();

    @SuppressLint("StaticFieldLeak")
    private static FinishFragment instance;

    private List<Specialty> specialtyList = new ArrayList<>();


    private MainActivity activity;

    public static FinishFragment getInstance() {
        if (instance == null)
            instance = new FinishFragment();

        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        if (activity != null)
            activity.navigation.setVisibility(View.INVISIBLE);
        setInitialData();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_finish, container, false);


        Button questAgainButton = view.findViewById(R.id.quest_again_btn);
        questAgainButton.setOnClickListener(questAgainButtonOnClickListener);

        RecyclerView recyclerView = view.findViewById(R.id.specialties);
        SpecialtyAdapter adapter = new SpecialtyAdapter(specialtyList, recyclerView, activity);

        Log.d(TAG, "specialtyList: " + specialtyList);
        Log.d(TAG, "adapter: " + adapter);

        recyclerView.setAdapter(adapter);
        return view;
    }

    private void setInitialData() {
        specialtyList.add(new Specialty("Cетевое и системное администрирование", "В настоящее время нет ни одной области науки и техники, в которых не применялась бы вычислительная техника. \n" +
                "С приходом в нашу жизнь компьютерной техники на рынке труда набирает популярность профессия, связанная с компьютерными системами и комплексами. \n" +
                "Представить себе жизнь без компьютера и мобильного телефона не способен ни один современный человек. Бурное развитие электроники породило необходимость в специалистах, которые могут обслуживать компьютерную технику. \n" +
                "В настоящее время многие способные молодые люди стараются получить образование в сфере компьютерных технологий. \n" +
                "Поэтому, одной из самых востребованных профессий в области электроники и вычислительной техники, является сетвой и системный администратор. \n"));
        specialtyList.add(new Specialty("Организация и технология защиты информации", "Профессиональная деятельность специалиста данного профиля включает в себя обеспечение защиты информации с применением разработанных методик и программ, анализ существующих методов и средств, используемых для контроля и защиты информации, разработку предложений по их совершенствованию и повышению эффективности.\n" +
                "\n" +
                "В ходе обучения выпускники приобретают навыки сбора и анализа материалов с целью выработки и принятия мер по обеспечению защиты данных и выявлению возможных каналов утечки информации, представляющих служебную, коммерческую, военную и государственные тайны.\n" +
                "\n" +
                "Выпускники специальности «Организация и технология защиты информации» умеют анализировать корпоративные сети на уязвимость и открытость, способны противодействовать атакам на вычислительные сети.\n" +
                "\n" +
                "В условиях растущих темпов информатизации общества возникает естественная необходимость построения эффективной системы защиты информационных ресурсов. Постоянное увеличение объема конфиденциальной информации, широкое использование различных технических свойств для ее обработки, хранения и передачи, появление новых методов и средств несанкционированного доступа к информации требуют подготовки высококвалифицированных специалистов по защите информации.\n" +
                "\n" +
                "Специальность «Организация и технология защиты информации» обеспечивает подготовку специалистов широкого профиля, способных как организовывать, так и осуществлять защиту различных видов конфиденциальной информации по всем направлениям защиты\n" +
                "Организация и технология защиты информации – одна из наиболее востребованных специальностей в эру информационных войн. Каждая компания стремится максимально защитить свои данные.\n" +
                "\n" +
                "Область профессиональной деятельности выпускников: проведение работ по документационному и организационно-технологическому обеспечению защиты информации в организациях различных структур и отраслевой направленности.\n" +
                "\n"));
        specialtyList.add(new Specialty("Обеспечение информационной безовасности автоматизированных систем", "Обеспечение информационной безопасности — это динамично развивающаяся область науки и техники, охватывающая криптографические, математические, программно-аппаратные, технические, правовые и организационные аспекты обеспечения безопасности информации при ее приеме, обработке, хранении и передаче в автоматизированных электронно-вычислительных системах и сетях. \n" +
                "В современном мире развивающихся технологий, передачи важнейших, в том числе и финансовых данных, по сетям - значимость такого специалиста просто неоценима. Благодаря дополнительным дисциплинам и учебным практикам студент получает знания в области устройства и обслуживания компьютерного оборудования, сетей и периферии. Наряду с профильным использованием специалиста, возможно, его использование в должности сетевого и системного администратора.\n" +
                "\n" +
                "Область профессиональной деятельности выпускников: организация и проведение работ по обеспечению защиты автоматизированных систем в организациях различных структур и отраслевой направленности."));
        specialtyList.add(new Specialty("Прикладная информатика (по отраслям)", "Быть выпускником такой специальности, как “Прикладная информатика“, значит быть на сто процентов уверенным, что благодаря автоматизации обыденных процессов, информационных технологий, технологий обработки и сбора информации в жизнь современного человека можно внести простоту и удобство.\n" +
                "Но кому подходит такая профессия и каковы ее особенности? Разберемся вместе.\n" +
                "Легче всего учиться будет тому, кто хоть немного разбирается в компьютерах и современных технологиях, а также проявляет к ним небывалый интерес, но это не значит, что необходимо уметь с закрытыми глазами переустанавливать программное обеспечение. Тут важно верить в то, что за будущим стоит развитие технологий! И то, что без участия человека информационные технологии ничего не значат.\n" +
                "Прикладная информатика занимается изучением информационных технологий, которые применяются где-либо. В прикладной информатике специалист сочетает навыки и умения, по построению информационной среды, простой и комфортной по применению. Прикладная информатика изучает информационные технологии, применяемые где-либо. Специалист, занятый в профессиях, связанных с информатикой, сочетает умения и навыки по построению информационной среды, удобной и простой для применения, а также оптимально соответствующей задаче познания в какой-либо выбранной им предметной области.\n"));
        specialtyList.add(new Specialty("Информационные системы и программирование", "Сегодня в современном обществе невозможно представить любое предприятие, организацию, технологический процесс или производство, учебный процесс или индустрию развлечений без использования компьютерной техники и программного обеспечения.\n" +
                "Информационные технологии, совершенствуясь сами, видоизменяют бизнес: механизмы его ведения, способы коммуникации, оказания услуг и производства товаров – идет построение информационного общества.\n" +
                "Именно поэтому специальность \"Информационные системы и программирование\" является одной из наиболее востребованных не только в России, но и в мире.\n"));
    }

    private View.OnClickListener questAgainButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            activity.resetQuest();
        }
    };
}
